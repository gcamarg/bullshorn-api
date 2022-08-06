package com.camargo.bullshorn.appuser;

import com.camargo.bullshorn.registration.token.ConfirmationToken;
import com.camargo.bullshorn.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {

        return appUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with %s not found", username))
        );
    }

    public String singUpUser(AppUser appUser) {

        boolean emailExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        boolean usernameExists = appUserRepository.findByUsername(appUser.getUsername()).isPresent();

        if (emailExists) {
            throw new IllegalStateException("Email already registered");
        } else if (usernameExists) {
            throw new IllegalStateException("Username is not available");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public void enableUser(String username) {
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(
                () -> new IllegalStateException("Email not found."));
        appUserRepository.enableUser(username);
    }
}
