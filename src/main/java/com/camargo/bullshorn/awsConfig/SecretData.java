package com.camargo.bullshorn.awsConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecretData {

    private String client_domain;
    private String database_user;
    private String database_password;
    private String emailSender_address;
    private String emailSender_password;
    private String token_secret;
    private String finnhub_token;

}
