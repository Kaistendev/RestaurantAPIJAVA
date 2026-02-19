package dev.kaisten.RestaurantAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Registro público
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()

                        // Auth: Login requiere credenciales válidas
                        .requestMatchers("/api/v1/auth/**").authenticated()

                        // Lectura pública para Menús, Platos y Restaurantes
                        .requestMatchers(HttpMethod.GET, "/api/v1/menus/**", "/api/v1/dishes/**",
                                "/api/v1/restaurants/**")
                        .permitAll()

                        // Solo RESTAURANT puede gestionar Platos, Menús y Restaurantes (Mutaciones)
                        .requestMatchers("/api/v1/dishes/**", "/api/v1/menus/**", "/api/v1/restaurants/**")
                        .hasRole("RESTAURANT")

                        // Gestión de Mesas: Solo RESTAURANT puede crear/editar/borrar
                        .requestMatchers(HttpMethod.GET, "/api/v1/tables/**").hasAnyRole("RESTAURANT", "CLIENT")
                        .requestMatchers("/api/v1/tables/**").hasRole("RESTAURANT")

                        // Perfil de usuario: Cualquier usuario autenticado
                        .requestMatchers("/api/v1/users/**").authenticated()

                        .anyRequest().authenticated())
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
