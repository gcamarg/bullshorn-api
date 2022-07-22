package com.camargo.bullshorn.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping
    public ResponseEntity register(@RequestBody RegistrationRequest request) {
        if(registrationService.register(request)){
           return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
    }

    @GetMapping(path = "/confirmation")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }


}
