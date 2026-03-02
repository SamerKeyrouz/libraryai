package com.samer.libraryai.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    @GetMapping("/api/auth/me")
    public Map<String, Object> me(Authentication authentication) {

        // not logged in
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User user)) {
            return Map.of(
                    "authenticated", false
            );
        }

        String email = (String) user.getAttributes().get("email");

        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Map.of(
                "authenticated", true,
                "email", email,
                "roles", roles
        );
    }
}