package com.camargo.bullshorn.registration;

import com.camargo.bullshorn.appuser.AppUser;
import com.camargo.bullshorn.appuser.AppUserRole;
import com.camargo.bullshorn.appuser.AppUserService;
import com.camargo.bullshorn.email.EmailLayout;
import com.camargo.bullshorn.email.EmailSender;
import com.camargo.bullshorn.registration.token.ConfirmationToken;
import com.camargo.bullshorn.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public boolean register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) throw new IllegalStateException("Email not valid");

        String token = appUserService.singUpUser(
                new AppUser(request.getUserName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER));
        String confirmationLink = "http://localhost:3000/confirm/"+token;
        emailSender.send(request.getEmail(), EmailLayout.buildEmail(request.getUserName(), confirmationLink));
        return true;

    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found."));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email has already been confirmed.");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired.");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        appUserService.enableUser(confirmationToken.getAppUser().getUsername());
        return "Email confirmed";
    }
}
