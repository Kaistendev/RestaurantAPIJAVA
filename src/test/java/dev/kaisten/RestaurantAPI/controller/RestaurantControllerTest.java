package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.dto.RestaurantRequestDTO;
import dev.kaisten.RestaurantAPI.dto.RestaurantResponseDTO;
import dev.kaisten.RestaurantAPI.service.RestaurantService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc
@Import({ dev.kaisten.RestaurantAPI.config.SecurityConfig.class,
        dev.kaisten.RestaurantAPI.config.JwtAuthenticationFilter.class })
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private dev.kaisten.RestaurantAPI.config.JwtService jwtService;

    @MockitoBean
    private org.springframework.security.authentication.AuthenticationProvider authenticationProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getAllRestaurants_shouldReturnRestaurants() throws Exception {
        RestaurantResponseDTO restaurant = new RestaurantResponseDTO(1L, "Great Food", "Main St 123",
                Collections.emptyList(), Collections.emptyList());
        Page<RestaurantResponseDTO> page = new PageImpl<>(Collections.singletonList(restaurant));

        when(restaurantService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/restaurants"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Great Food"));
    }

    @Test
    public void createRestaurant_whenRestaurant_shouldCreateRestaurant() throws Exception {
        RestaurantRequestDTO request = new RestaurantRequestDTO("New Restaurant", "Address", List.of(1L), List.of(1L));
        RestaurantResponseDTO response = new RestaurantResponseDTO(1L, "New Restaurant", "Address",
                Collections.emptyList(), Collections.emptyList());

        when(restaurantService.create(any(RestaurantRequestDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/restaurants")
                .with(user("admin").roles("RESTAURANT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Restaurant"));
    }

    @Test
    public void updateRestaurant_whenRestaurant_shouldUpdateRestaurant() throws Exception {
        RestaurantRequestDTO request = new RestaurantRequestDTO("Updated Restaurant", "Address", List.of(1L),
                List.of(1L));
        RestaurantResponseDTO response = new RestaurantResponseDTO(1L, "Updated Restaurant", "Address",
                Collections.emptyList(), Collections.emptyList());

        when(restaurantService.update(eq(1L), any(RestaurantRequestDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/restaurants/1")
                .with(user("admin").roles("RESTAURANT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Restaurant"));
    }

    @Test
    public void deleteRestaurant_whenRestaurant_shouldDeleteRestaurant() throws Exception {
        doNothing().when(restaurantService).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/restaurants/1")
                .with(user("admin").roles("RESTAURANT")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
