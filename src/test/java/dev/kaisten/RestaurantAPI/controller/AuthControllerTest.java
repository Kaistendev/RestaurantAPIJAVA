package dev.kaisten.RestaurantAPI.controller;

import tools.jackson.databind.ObjectMapper;
import dev.kaisten.RestaurantAPI.config.JwtService;
import dev.kaisten.RestaurantAPI.dto.LoginRequestDTO;
import dev.kaisten.RestaurantAPI.dto.RegisterRequestDTO;
import dev.kaisten.RestaurantAPI.entity.User;
import dev.kaisten.RestaurantAPI.entity.UserRole;
import dev.kaisten.RestaurantAPI.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for this unit test to focus on controller logic
@Import({ dev.kaisten.RestaurantAPI.config.SecurityConfig.class,
                dev.kaisten.RestaurantAPI.config.JwtAuthenticationFilter.class })
public class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private UserRepository userRepository;

        @MockitoBean
        private UserDetailsService userDetailsService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private AuthenticationManager authenticationManager;

        @MockitoBean
        private PasswordEncoder passwordEncoder;

        @MockitoBean
        private dev.kaisten.RestaurantAPI.service.RefreshTokenService refreshTokenService;

        @MockitoBean
        private org.springframework.security.authentication.AuthenticationProvider authenticationProvider;

        @Test
        public void login_whenValidCredentials_shouldReturnToken() throws Exception {
                User user = User.builder()
                                .email("test@example.com")
                                .firstName("Test")
                                .lastName("User")
                                .role(UserRole.CLIENT)
                                .password("encodedPassword")
                                .build();

                LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "password");
                var refreshToken = dev.kaisten.RestaurantAPI.entity.RefreshToken.builder()
                                .token("refresh_token")
                                .build();

                when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
                when(jwtService.generateToken(any(User.class))).thenReturn("jwt_token");
                when(refreshTokenService.createRefreshToken(any())).thenReturn(refreshToken);

                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("jwt_token"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value("refresh_token"));
        }

        @Test
        public void register_whenValidRequest_shouldReturnToken() throws Exception {
                RegisterRequestDTO registerRequest = new RegisterRequestDTO(
                                "New", "User", "new@example.com", "password", UserRole.CLIENT);

                when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
                when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
                when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
                when(jwtService.generateToken(any(User.class))).thenReturn("jwt_token");
                var refreshToken = dev.kaisten.RestaurantAPI.entity.RefreshToken.builder()
                                .token("refresh_token")
                                .build();
                when(refreshTokenService.createRefreshToken(any())).thenReturn(refreshToken);

                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("new@example.com"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("jwt_token"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value("refresh_token"));
        }
}
