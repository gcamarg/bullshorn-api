package com.camargo.bullshorn.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<Object> register(@RequestBody RegistrationRequest request) {
        String registerResponse = registrationService.register(request);
        if(registerResponse.equals("success")){
            return new ResponseEntity<>(registerResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(registerResponse, HttpStatus.EXPECTATION_FAILED);
    }

    @GetMapping(path = "/confirmation")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }


}
