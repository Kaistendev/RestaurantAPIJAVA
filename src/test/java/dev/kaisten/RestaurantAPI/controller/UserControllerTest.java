package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.dto.UserRequestDTO;
import dev.kaisten.RestaurantAPI.dto.UserResponseDTO;
import dev.kaisten.RestaurantAPI.entity.UserRole;
import dev.kaisten.RestaurantAPI.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import({ dev.kaisten.RestaurantAPI.config.SecurityConfig.class,
                dev.kaisten.RestaurantAPI.config.JwtAuthenticationFilter.class })
public class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private UserService userService;

        @MockitoBean
        private dev.kaisten.RestaurantAPI.repository.UserRepository userRepository;

        @MockitoBean
        private UserDetailsService userDetailsService;

        @MockitoBean
        private dev.kaisten.RestaurantAPI.config.JwtService jwtService;

        @MockitoBean
        private org.springframework.security.authentication.AuthenticationProvider authenticationProvider;

        private ObjectMapper objectMapper = new ObjectMapper();

        @Test
        public void getAllUsers_whenAuthenticated_shouldReturnUsers() throws Exception {
                UserResponseDTO userRes = new UserResponseDTO(1L, "John", "Doe", "john.doe@example.com",
                                UserRole.CLIENT);
                Page<UserResponseDTO> page = new PageImpl<>(Collections.singletonList(userRes));

                when(userService.findAll(any(Pageable.class))).thenReturn(page);

                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                                .with(user("admin").roles("RESTAURANT")))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email")
                                                .value("john.doe@example.com"));
        }

        @Test
        public void createUser_shouldRegisterPublicly() throws Exception {
                UserRequestDTO request = new UserRequestDTO("John", "Doe", "john.doe@example.com", "password123",
                                UserRole.CLIENT);
                UserResponseDTO response = new UserResponseDTO(1L, "John", "Doe", "john.doe@example.com",
                                UserRole.CLIENT);

                when(userRepository.findByEmail("john.doe@example.com")).thenReturn(java.util.Optional.empty());
                when(userService.create(any(UserRequestDTO.class))).thenReturn(response);

                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"));
        }

        @Test
        public void deleteUser_whenAuthenticated_shouldDeleteUser() throws Exception {
                doNothing().when(userService).delete(1L);
                mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/1")
                                .with(user("admin").roles("RESTAURANT")))
                                .andExpect(MockMvcResultMatchers.status().isNoContent());
        }
}
