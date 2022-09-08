package com.camargo.bullshorn.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JWTValidationFilter extends BasicAuthenticationFilter {

    public static final String ATTRIBUTE_BEARER = "Bearer ";
    private String TOKEN_SECRET;
    public JWTValidationFilter(AuthenticationManager authenticationManager, String TOKEN_SECRET) {

        super(authenticationManager);
        this.TOKEN_SECRET = TOKEN_SECRET;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String tokenAttribute = request.getHeader(AUTHORIZATION);

        if (tokenAttribute == null || !tokenAttribute.startsWith(ATTRIBUTE_BEARER)) {
            chain.doFilter(request, response);
            return;
        }
        String token = tokenAttribute.replace(ATTRIBUTE_BEARER, "");
        try {
            UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token, response);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
        }catch (Exception ex){
            Map<String, String> error = new HashMap<>();
            error.put("Error_message", ex.getMessage());
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(String token, HttpServletResponse response) throws IOException {
        try {
            String user = JWT.require(Algorithm.HMAC512(TOKEN_SECRET))
                    .build()
                    .verify(token)
                    .getSubject();
            if( user == null) return null;
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

        } catch(Exception ex){
            throw ex;
        }


    }
}
