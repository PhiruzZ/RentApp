package com.example.rentapp.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.response.AuthResponse;
import com.example.rentapp.service.ClientAuthenticationService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    @Value("${util.jwt.secret}")
    private String jwtSecret;

    @Value("${util.jwt.expiration}")
    private String expiration;

    public AuthResponse generateJwtToken(UserEntity user) {
        log.info("Generating JWT token for client with ID : {}" , user.getEmail());

        Map<String,Object> claims = new HashMap<>();
        claims.put("role",user.getRole());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,Integer.parseInt(expiration));
        Date current = new Date();
        Date expirationDate = calendar.getTime();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(current)
                .setExpiration(expirationDate)
                .addClaims(claims)
                .signWith(key());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtBuilder.compact());
        authResponse.setTokenType("Bearer");
        authResponse.setExpiresIn(Integer.parseInt(expiration));
        return authResponse;
    }
    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwt(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;

        } catch (Exception e) {
            log.error("Error during validating the token");
        }
        return false;
    }

    public String getClientId(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public DecodedJWT getJwtToken() {
        ClientAuthenticationService auth = (ClientAuthenticationService) SecurityContextHolder.getContext().getAuthentication();
        return JWT.decode(auth.getToken());

    }

    public Long getClientId() {
        return Long.parseLong(getJwtToken().getSubject());
    }
}
