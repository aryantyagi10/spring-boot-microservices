package com.ecommerce.user_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // 1. Read Secret from application.yml
    @Value("${jwt.secret}")
    private String secret;

    // 2. Read Expiration from application.yml
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public String generateToken(String userName){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName){
        return Jwts.builder()  //This creates token(header.payload.signature) internally
                .setClaims(claims)
                .setSubject(userName) //Subjet = Identity of the token owner
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) //Means Token will expire at currentTime + jwtExpiration
                .signWith(getSignKey(), SignatureAlgorithm.HS256)  //Use HMAC with SHA-256 Hashing Algo
                .compact();  //Convert Everything into final JWT String : xxxx.yyyy.zzz ; Header.payload.Signature
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}


/* getSignKey() --> Convert secret into a proper cryptographic key.
            Step-by-step:
                1. Takes secret from config
                2. Decodes it into raw bytes
                3. Converts raw bytes into secure HMAC Key object
                4. Ensures correct length
                5. Returns Key for signing

signWith() --> It does not immediately create the signature.
               It just stores:
                    - The Key
                    - The algorithm
               The actual signature is created later inside .compact()


JWT Generation:
                1. Create header
                2. Create payload
                3. Base64URL encode both
                4. Prepare secret key (geySignKey)
                5. Generate HMAC signature
                6. BASE64URL encode signature
                7. Join with dots
                8. Return String


--What .compact() Actually does Internally?
    1. Convert header Map -> JSON
    2. Convert payload Map -> JSON
    3. BASE64URL encode header JSON
    4. BASE64URL encode payload JSON
    5. Generate Signature
    6. BASE64URL encode signature
    7. Join everything with dots
    8. Return final String

* */
