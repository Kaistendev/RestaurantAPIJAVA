package dev.kaisten.RestaurantAPI.controller;

import tools.jackson.databind.ObjectMapper;
import dev.kaisten.RestaurantAPI.dto.DishRequestDTO;
import dev.kaisten.RestaurantAPI.dto.DishResponseDTO;
import dev.kaisten.RestaurantAPI.service.DishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@WebMvcTest(DishController.class)
@AutoConfigureMockMvc
@Import(dev.kaisten.RestaurantAPI.config.SecurityConfig.class)
public class DishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DishService dishService;

    @MockitoBean
    private UserDetailsService userDetailsService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllDishes_shouldReturnDishes() throws Exception {
        DishResponseDTO dish = new DishResponseDTO(1L, "Test Dish", "Test Description", BigDecimal.TEN, true);
        Page<DishResponseDTO> page = new PageImpl<>(Collections.singletonList(dish));

        when(dishService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dishes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Test Dish"));
    }
    
    @Test
    public void getDishById_shouldReturnDish() throws Exception {
        DishResponseDTO dish = new DishResponseDTO(1L, "Test Dish", "Test Description", BigDecimal.TEN, true);
        when(dishService.findById(1L)).thenReturn(dish);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dishes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Dish"));
    }

    @Test
    public void createDish_whenClient_shouldReturnForbidden() throws Exception {
        DishRequestDTO request = new DishRequestDTO("New Dish", "Description", BigDecimal.ONE, true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/dishes")
                        .with(user("client").roles("CLIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void createDish_whenRestaurant_shouldCreateDish() throws Exception {
        DishRequestDTO request = new DishRequestDTO("New Dish", "Description", BigDecimal.ONE, true);
        DishResponseDTO response = new DishResponseDTO(1L, "New Dish", "Description", BigDecimal.ONE, true);
        
        when(dishService.create(any(DishRequestDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/dishes")
                        .with(user("admin").roles("RESTAURANT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Dish"));
    }

    @Test
    public void updateDish_whenRestaurant_shouldUpdateDish() throws Exception {
        DishRequestDTO request = new DishRequestDTO("Updated Dish", "Description", BigDecimal.ONE, true);
        DishResponseDTO response = new DishResponseDTO(1L, "Updated Dish", "Description", BigDecimal.ONE, true);
        
        when(dishService.update(eq(1L), any(DishRequestDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/dishes/1")
                        .with(user("admin").roles("RESTAURANT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Dish"));
    }

    @Test
    public void deleteDish_whenRestaurant_shouldDeleteDish() throws Exception {
        doNothing().when(dishService).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/dishes/1")
                        .with(user("admin").roles("RESTAURANT")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
