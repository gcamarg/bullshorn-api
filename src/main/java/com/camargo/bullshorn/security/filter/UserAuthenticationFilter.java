package com.camargo.bullshorn.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.camargo.bullshorn.appuser.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    public final String TOKEN_SECRET;
    private final int TOKEN_EXPIRATION;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AppUser appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException("Failed to authenticate: ", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        String access_token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(TOKEN_SECRET));
        String refresh_token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC512(TOKEN_SECRET));

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response,  failed);
    }
}
