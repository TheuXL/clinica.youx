package com.clinica.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String ISSUER_URI = "https://dev-theuxl.us.auth0.com/";
    private static final String SCOPE_READ = "SCOPE_read:pacientes";
    private static final String SCOPE_WRITE = "SCOPE_write:pacientes";
    private static final String SCOPE_CREATE = "SCOPE_create:pacientes";
    private static final String SCOPE_UPDATE = "SCOPE_update:pacientes";
    private static final String SCOPE_DELETE = "SCOPE_delete:pacientes";
    private static final String SCOPE_FULL_CONTROL = "SCOPE_full_control";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/pacientes").hasAuthority(SCOPE_READ)
                .requestMatchers("/api/pacientes/**").hasAuthority(SCOPE_WRITE)
                .requestMatchers("/api/pacientes").hasAuthority(SCOPE_CREATE)
                .requestMatchers("/api/pacientes/**").hasAuthority(SCOPE_UPDATE)
                .requestMatchers("/api/pacientes/**").hasAuthority(SCOPE_DELETE)
                .requestMatchers("/api/medicos/**", "/api/enfermeiros/**").hasAuthority(SCOPE_FULL_CONTROL)
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(ISSUER_URI);
    }
}
