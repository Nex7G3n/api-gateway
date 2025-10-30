package com.autoparts.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.cors(Customizer.withDefaults()); // Habilitar CORS con la configuración por defecto

        http
            // Deshabilita CSRF (útil en APIs REST)
            .csrf(csrf -> csrf.disable())

            // Define las políticas de acceso
            .authorizeExchange(exchanges -> exchanges
                // Endpoints públicos (no requieren autenticación)
                .pathMatchers(
                    "/actuator/health",
                    "/actuator/info",
                    "/login/**",
                    "/api/auto/**",
                    "/clients/**"
                ).permitAll()

                // Todo lo demás requiere autenticación OAuth2
                .anyExchange().authenticated()
            )

            // Habilita login con Google OAuth2
            .oauth2Login(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174", "https://nonloxodromic-harriette-inertly.ngrok-free.dev", "https://36ef3f9e6f50.ngrok-free.app/")); // Añade los orígenes permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
