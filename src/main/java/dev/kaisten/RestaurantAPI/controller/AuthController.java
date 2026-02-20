package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.config.JwtService;
import dev.kaisten.RestaurantAPI.dto.AuthResponseDTO;
import dev.kaisten.RestaurantAPI.dto.LoginRequestDTO;
import dev.kaisten.RestaurantAPI.dto.RegisterRequestDTO;
import dev.kaisten.RestaurantAPI.entity.User;
import dev.kaisten.RestaurantAPI.entity.UserRole;
import dev.kaisten.RestaurantAPI.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;

        @PostMapping("/register")
        public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
                UserRole role = request.role() != null ? request.role() : UserRole.CLIENT;

                if (userRepository.findByEmail(request.email()).isPresent()) {
                        return ResponseEntity.badRequest().build();
                }

                User user = User.builder()
                                .firstName(request.firstName())
                                .lastName(request.lastName())
                                .email(request.email())
                                .password(passwordEncoder.encode(request.password()))
                                .role(role)
                                .build();

                userRepository.save(user);

                String jwtToken = jwtService.generateToken(user);

                return ResponseEntity.ok(new AuthResponseDTO(
                                user.getEmail(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getRole(),
                                jwtToken));
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.email(),
                                                request.password()));

                User user = userRepository.findByEmail(request.email())
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                String jwtToken = jwtService.generateToken(user);

                return ResponseEntity.ok(new AuthResponseDTO(
                                user.getEmail(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getRole(),
                                jwtToken));
        }
}
