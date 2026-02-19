package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.dto.RestaurantTableRequestDTO;
import dev.kaisten.RestaurantAPI.dto.RestaurantTableResponseDTO;
import dev.kaisten.RestaurantAPI.service.RestaurantTableService;
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

@WebMvcTest(RestaurantTableController.class)
@AutoConfigureMockMvc
@Import(dev.kaisten.RestaurantAPI.config.SecurityConfig.class)
public class RestaurantTableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantTableService tableService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllTables_whenClient_shouldReturnTables() throws Exception {
        RestaurantTableResponseDTO table = new RestaurantTableResponseDTO(1L, 10, 4, true, 1L);
        Page<RestaurantTableResponseDTO> page = new PageImpl<>(Collections.singletonList(table));

        when(tableService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tables")
                .with(user("client").roles("CLIENT")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].tableNumber").value(10));
    }

    @Test
    public void createTable_whenClient_shouldReturnForbidden() throws Exception {
        RestaurantTableRequestDTO request = new RestaurantTableRequestDTO(10, 4, true, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tables")
                .with(user("client").roles("CLIENT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void createTable_whenRestaurant_shouldCreateTable() throws Exception {
        RestaurantTableRequestDTO request = new RestaurantTableRequestDTO(10, 4, true, 1L);
        RestaurantTableResponseDTO response = new RestaurantTableResponseDTO(1L, 10, 4, true, 1L);

        when(tableService.create(any(RestaurantTableRequestDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tables")
                .with(user("admin").roles("RESTAURANT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tableNumber").value(10));
    }

    @Test
    public void deleteTable_whenRestaurant_shouldDeleteTable() throws Exception {
        doNothing().when(tableService).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tables/1")
                .with(user("admin").roles("RESTAURANT")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
