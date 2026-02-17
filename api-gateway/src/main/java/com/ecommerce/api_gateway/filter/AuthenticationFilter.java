package com.ecommerce.api_gateway.filter;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Value("${jwt.secret}")
    private String secret;

    public AuthenticationFilter(){
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config){
        return (exchange, chain) -> {
            // 1. Check Header
            if(!exchange.getRequest()
                    .getHeaders()
                    .containsHeader(HttpHeaders.AUTHORIZATION)){
                throw new RuntimeException("Missing Authorization Header");
            }

            // 2. Extract Token
            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if(authHeader != null && authHeader.startsWith("Bearer ")){
                authHeader = authHeader.substring(7);
            }

            // 3. Validate
            try {
                validateToken(authHeader);
            } catch (Exception e){
                throw new RuntimeException("Unauthorized Access");
            }

            // 4. Forward
            return chain.filter(exchange);
        };
    }


    private void validateToken(String token){
        Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);  //This is where actual validation happens
    }


    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static class Config{ }
}





/*
--  .parserClaimsJws(token) - This where the actual validation happens
                              Internally parser does:
                                1. Split token into 3 parts
                                2. Base64 decode header & payload
                                3. Extract signature
                                4. Recalculate signature using secret
                                5. Compare both signature
                                6. Check expiration
                                7. If valid -> return Claims
                                8. If invalid -> throw exception
 */