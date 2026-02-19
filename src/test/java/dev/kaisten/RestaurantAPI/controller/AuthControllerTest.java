package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.entity.User;
import dev.kaisten.RestaurantAPI.entity.UserRole;
import dev.kaisten.RestaurantAPI.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@Import(dev.kaisten.RestaurantAPI.config.SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    public void login_whenValidCredentials_shouldReturnUserInfo() throws Exception {
        User user = User.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .role(UserRole.CLIENT)
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/login")
                        .with(user("test@example.com").roles("CLIENT")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("CLIENT"));
    }

    @Test
    public void login_whenNotAuthenticated_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/login"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
