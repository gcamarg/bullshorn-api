package com.camargo.bullshorn.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    @Value("${emailSender.address}")
    private String emailSender;

    @Override
    @Async
    public void send(String to, String emailBody) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailBody, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email.");
            helper.setFrom(emailSender);
            mailSender.send(mimeMessage);
        }catch(MessagingException e){
            LOGGER.error("Failed to send email ", e);
            throw new IllegalStateException("Failed to send email.");
        }
    }
}