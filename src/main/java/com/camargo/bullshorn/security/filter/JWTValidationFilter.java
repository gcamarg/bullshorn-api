package com.camargo.bullshorn.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

public class JWTValidationFilter extends BasicAuthenticationFilter {

    public static final String HEADER_ATTRIBUTE = "Authorization";
    public static final String ATTRIBUTE_BEARER = "Bearer ";
    private String TOKEN_SECRET;
    public JWTValidationFilter(AuthenticationManager authenticationManager, String TOKEN_SECRET) {

        super(authenticationManager);
        this.TOKEN_SECRET = TOKEN_SECRET;
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
        String user = JWT.require(Algorithm.HMAC512(TOKEN_SECRET))
                .build()
                .verify(token)
                .getSubject();

        if( user == null) return null;

        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }
}
