package com.project.test.ordermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.test.ordermanagement.model.Resale;
import com.project.test.ordermanagement.service.ResaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResaleController.class)
class ResaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResaleService resaleService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Resale resale;

    @BeforeEach
    void setUp() {
        resale = Resale.builder()
                .businessName("Comercial Exemplo LTDA")
                .contractName("João da Silva")
                .deliveryAddress("Rua da Entrega, 456")
                .email("contato@exemplo.com")
                .phone("11999887766")
                .taxId(UUID.randomUUID().toString().substring(0, 13))
                .tradeName("Comercial Exemplo")
                .build();
    }

    @Test
    void shouldCreateResale() throws Exception {
        when(resaleService.save(any(Resale.class))).thenReturn(resale);

        mockMvc.perform(post("/api/resales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resale)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resale.getId()));
    }

    @Test
    void shouldGetResaleById() throws Exception {
        when(resaleService.findById(1L)).thenReturn(Optional.of(resale));

        mockMvc.perform(get("/api/resales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resale.getId()));
    }

    @Test
    void shouldReturnNotFoundIfResaleNotExists() throws Exception {
        when(resaleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/resales/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllResales() throws Exception {
        when(resaleService.findAll()).thenReturn(List.of(resale));

        mockMvc.perform(get("/api/resales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(resale.getId()));
    }

    @Test
    void shouldUpdateResale() throws Exception {
        when(resaleService.update(eq(1L), any(Resale.class))).thenReturn(Optional.of(resale));

        mockMvc.perform(put("/api/resales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resale)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resale.getId()));
    }

    @Test
    void shouldDeleteResale() throws Exception {
        doNothing().when(resaleService).delete(1L);

        mockMvc.perform(delete("/api/resales/1"))
                .andExpect(status().isNoContent());
    }

}
