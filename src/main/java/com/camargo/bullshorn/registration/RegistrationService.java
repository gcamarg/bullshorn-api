package com.camargo.bullshorn.registration;

import com.camargo.bullshorn.appuser.AppUser;
import com.camargo.bullshorn.appuser.AppUserRole;
import com.camargo.bullshorn.appuser.AppUserService;
import com.camargo.bullshorn.email.EmailLayout;
import com.camargo.bullshorn.email.EmailSender;
import com.camargo.bullshorn.registration.token.ConfirmationToken;
import com.camargo.bullshorn.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    @Value("${client.url}")
    private java.lang.String CLIENT_URL;
    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) throw new IllegalStateException("Email not valid");

        try {
            String token = appUserService.singUpUser(
                    new AppUser(request.getUserName(),
                            request.getEmail(),
                            request.getPassword(),
                            AppUserRole.USER));
            String confirmationLink = CLIENT_URL + "/confirm/" + token;
            emailSender.send(request.getEmail(), EmailLayout.buildEmail(request.getUserName(), confirmationLink));
            return "success";
        } catch(IllegalStateException e) {
            return e.getMessage();
        }
    }

    public ResponseEntity<String> confirmToken(String token) {

        try {

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
            return  new ResponseEntity<>("Email confirmed", HttpStatus.OK);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }
}
