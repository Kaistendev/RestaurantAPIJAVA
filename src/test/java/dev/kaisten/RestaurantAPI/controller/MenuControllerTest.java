package dev.kaisten.RestaurantAPI.controller;

import dev.kaisten.RestaurantAPI.dto.MenuRequestDTO;
import dev.kaisten.RestaurantAPI.dto.MenuResponseDTO;
import dev.kaisten.RestaurantAPI.service.MenuService;
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

@WebMvcTest(MenuController.class)
@AutoConfigureMockMvc
@Import({ dev.kaisten.RestaurantAPI.config.SecurityConfig.class,
        dev.kaisten.RestaurantAPI.config.JwtAuthenticationFilter.class })
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuService menuService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private dev.kaisten.RestaurantAPI.config.JwtService jwtService;

    @MockitoBean
    private org.springframework.security.authentication.AuthenticationProvider authenticationProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getAllMenus_shouldReturnMenus() throws Exception {
        MenuResponseDTO menu = new MenuResponseDTO(1L, "Summer Menu", "Light dishes", Collections.emptyList());
        Page<MenuResponseDTO> page = new PageImpl<>(Collections.singletonList(menu));

        when(menuService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/menus"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Summer Menu"));
    }

    @Test
    public void createMenu_whenRestaurant_shouldCreateMenu() throws Exception {
        MenuRequestDTO request = new MenuRequestDTO("New Menu", "Description", List.of(1L));
        MenuResponseDTO response = new MenuResponseDTO(1L, "New Menu", "Description", Collections.emptyList());

        when(menuService.create(any(MenuRequestDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/menus")
                .with(user("admin").roles("RESTAURANT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Menu"));
    }

    @Test
    public void updateMenu_whenRestaurant_shouldUpdateMenu() throws Exception {
        MenuRequestDTO request = new MenuRequestDTO("Updated Menu", "Description", List.of(1L));
        MenuResponseDTO response = new MenuResponseDTO(1L, "Updated Menu", "Description", Collections.emptyList());

        when(menuService.update(eq(1L), any(MenuRequestDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/menus/1")
                .with(user("admin").roles("RESTAURANT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Menu"));
    }

    @Test
    public void deleteMenu_whenRestaurant_shouldDeleteMenu() throws Exception {
        doNothing().when(menuService).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/menus/1")
                .with(user("admin").roles("RESTAURANT")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
