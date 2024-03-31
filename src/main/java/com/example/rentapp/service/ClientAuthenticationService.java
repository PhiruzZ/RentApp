package com.example.rentapp.service;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class ClientAuthenticationService extends UsernamePasswordAuthenticationToken {
    private final String token;
    public ClientAuthenticationService(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String token) {
        super(principal, credentials, authorities);
        this.token=token;
    }

}
