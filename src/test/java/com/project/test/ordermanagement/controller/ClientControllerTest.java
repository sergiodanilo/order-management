package com.project.test.ordermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.test.ordermanagement.model.Client;
import com.project.test.ordermanagement.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Client client;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .name("João da Silva")
                .email("joao" + new Random().nextInt(1000) + "@gmail.com")
                .phone("11999990000")
                .address("Rua Exemplo, 123")
                .taxId(UUID.randomUUID().toString().substring(0, 13))
                .build();
    }

    @Test
    void shouldCreateClient() throws Exception {
        when(clientService.save(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(client.getId()));
    }

    @Test
    void shouldGetClientById() throws Exception {
        when(clientService.findById(1L)).thenReturn(Optional.of(client));

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(client.getId()));
    }

    @Test
    void shouldReturnNotFoundIfClientNotExists() throws Exception {
        when(clientService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllClients() throws Exception {
        when(clientService.findAll()).thenReturn(List.of(client));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(client.getId()));
    }

    @Test
    void shouldUpdateClient() throws Exception {
        when(clientService.update(eq(1L), any(Client.class))).thenReturn(Optional.of(client));

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(client.getId()));
    }

    @Test
    void shouldDeleteClient() throws Exception {
        doNothing().when(clientService).delete(1L);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());
    }

}
