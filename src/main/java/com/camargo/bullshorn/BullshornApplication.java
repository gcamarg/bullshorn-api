package com.camargo.bullshorn;

import com.camargo.bullshorn.awsConfig.SecretRetriever;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BullshornApplication {

    public static void main(String[] args) {
        SpringApplication.run(BullshornApplication.class, args);
        //new SpringApplicationBuilder(BullshornApplication.class).properties(SecretRetriever.getSecret()).run(args);
    }

}
