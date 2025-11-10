package com.example.facturacioninventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para permitir carritos anónimos
 *
 * IMPORTANTE: Copia este archivo a tu backend Spring Boot en:
 * src/main/java/com/example/facturacioninventario/config/SecurityConfig.java
 *
 * ACTUALIZACIÓN: Ahora permite TODAS las operaciones con carritos:
 * - POST /api/carritos (crear carrito)
 * - GET /api/carritos/{id} (consultar carrito)
 * - POST /api/carritos/{id}/items (agregar items) ← NUEVO
 * - DELETE /api/carritos/{id}/items/{itemId} (eliminar items) ← NUEVO
 * - PUT /api/carritos/{id}/items/{itemId} (actualizar items) ← NUEVO
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Deshabilitar CSRF para desarrollo
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos (sin autenticación)
                .requestMatchers("/api/productos/**").permitAll()
                .requestMatchers("/api/categorias/**").permitAll()

                // ← ACTUALIZADO: Permitir TODAS las operaciones con carritos
                .requestMatchers("/api/carritos/**").permitAll()

                // Permitir favoritos sin autenticación
                .requestMatchers("/api/favoritos/**").permitAll()

                // Todos los demás endpoints requieren autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
