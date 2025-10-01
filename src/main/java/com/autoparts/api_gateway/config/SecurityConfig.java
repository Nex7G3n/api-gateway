package com.autoparts.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http
            // Deshabilita CSRF para simplificar pruebas (no usar en producción sin revisar)
            .csrf(csrf -> csrf.disable())
            
            // Configura qué rutas son públicas y cuáles requieren autenticación
            .authorizeExchange(exchanges -> exchanges
                // Actuator accesible sin login
                .pathMatchers("/actuator/health", "/actuator/info", "/api/**").permitAll()
                // Login de Google accesible sin autenticación
                .pathMatchers("/login/**", "/api/**").permitAll()
                // Todo lo demás requiere estar autenticado
                .anyExchange().authenticated()
            )
            
            // Habilita login interactivo con Google OAuth2
            .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}