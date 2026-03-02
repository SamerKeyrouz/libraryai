package com.samer.libraryai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Static Angular build files
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/**/*.js",
                                "/**/*.css",
                                "/assets/**",
                                "/favicon.ico"
                        ).permitAll()

                        // Swagger + H2
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // -------------------------
                        // PUBLIC ACCESS
                        // -------------------------

                        // View + Search books
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").permitAll()

                        // AI allowed for everyone
                        .requestMatchers("/api/ai/**").permitAll()

                        // -------------------------
                        // AUTHENTICATED USERS
                        // -------------------------

                        // Borrow / Return requires login
                        .requestMatchers(HttpMethod.PUT, "/api/books/*/checkout").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/books/*/checkin").authenticated()

                        // -------------------------
                        // ADMIN ONLY
                        // -------------------------

                        .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")

                        .anyRequest().permitAll()
                )

                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo ->
                                userInfo.userAuthoritiesMapper(userAuthoritiesMapper())
                        )
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        // Needed for H2 console
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    // =============================
    // ROLE MAPPING
    // =============================
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {

        return authorities -> {

            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            // Every logged in user gets ROLE_USER
            mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            for (GrantedAuthority authority : authorities) {

                if (authority instanceof OAuth2UserAuthority oauth) {

                    Object emailObj = oauth.getAttributes().get("email");

                    if (emailObj != null) {
                        String email = emailObj.toString().toLowerCase();

                        // ADMIN EMAIL
                        if (email.equals("samer.key@hotmail.com") || email.equals("charles.saroufim@aspiresoftware.com") ) {
                            mappedAuthorities.add(
                                    new SimpleGrantedAuthority("ROLE_ADMIN")
                            );
                        }
                    }
                }
            }

            return mappedAuthorities;
        };
    }

    // =============================
    // CORS CONFIG
    // =============================
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {

        org.springframework.web.cors.CorsConfiguration configuration =
                new org.springframework.web.cors.CorsConfiguration();

        configuration.setAllowedOrigins(
                java.util.List.of(
                        "http://localhost:4200",
                        "https://libraryai.onrender.com"
                )
        );

        configuration.setAllowedMethods(
                java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                java.util.List.of("*")
        );

        configuration.setAllowCredentials(true);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}