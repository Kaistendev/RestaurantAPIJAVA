package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.dto.AuthResponseDTO;
import dev.kaisten.RestaurantAPI.entity.User;
import dev.kaisten.RestaurantAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @GetMapping("/login")
    public ResponseEntity<AuthResponseDTO> login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return ResponseEntity.status(401).build();
        }

        String email = auth.getName();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return ResponseEntity.ok(new AuthResponseDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        ));
    }
}
