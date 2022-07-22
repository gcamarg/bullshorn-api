package com.camargo.bullshorn.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

public class JWTValidationFilter extends BasicAuthenticationFilter {

    public static final String HEADER_ATTRIBUTE = "Authorization";
    public static final String ATTRIBUTE_BEARER = "Bearer ";

    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String tokenAttribute = request.getHeader(HEADER_ATTRIBUTE);

        if (tokenAttribute == null || !tokenAttribute.startsWith(ATTRIBUTE_BEARER)) {
            chain.doFilter(request, response);
            return;
        }
        String token = tokenAttribute.replace(ATTRIBUTE_BEARER, "");

        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(String token){
        String user = JWT.require(Algorithm.HMAC512(UserAuthenticationFilter.TOKEN_SECRET))
                .build()
                .verify(token)
                .getSubject();

        if( user == null) return null;

        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }
}
