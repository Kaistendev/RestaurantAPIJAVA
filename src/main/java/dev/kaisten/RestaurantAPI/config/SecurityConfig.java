package dev.kaisten.RestaurantAPI.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/menus/**",
                                                                "/api/v1/dishes/**", "/api/v1/restaurants/**")
                                                .permitAll()
                                                .requestMatchers("/api/v1/dishes/**", "/api/v1/menus/**",
                                                                "/api/v1/restaurants/**")
                                                .hasRole("RESTAURANT")
                                                .requestMatchers(HttpMethod.GET, "/api/v1/tables/**")
                                                .hasAnyRole("RESTAURANT", "CLIENT")
                                                .requestMatchers("/api/v1/tables/**").hasRole("RESTAURANT")
                                                .requestMatchers("/api/v1/users/**").authenticated()
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
