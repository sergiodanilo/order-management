package com.project.test.ordermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.test.ordermanagement.model.dto.OrderDTO;
import com.project.test.ordermanagement.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderDTO mockOrder;

    @BeforeEach
    void setUp() {
        mockOrder = OrderDTO.builder()
                .orderNumber(1L)
                .taxId("123456789")
                .date(LocalDateTime.now())
                .totalAmount(BigDecimal.valueOf(99.99))
                .orderItems(Set.of())
                .build();
    }

    @Test
    void testRegisterOrder_Success() throws Exception {
        when(orderService.registerOrder(any(OrderDTO.class))).thenReturn(Optional.of(mockOrder));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value(1L))
                .andExpect(jsonPath("$.taxId").value("123456789"));
    }

    @Test
    void testRegisterOrder_BadRequest() throws Exception {
        when(orderService.registerOrder(any(OrderDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockOrder)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        when(orderService.findById(eq(1L))).thenReturn(Optional.of(mockOrder));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value(1L))
                .andExpect(jsonPath("$.taxId").value("123456789"));
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        when(orderService.findById(eq(99L))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/99"))
                .andExpect(status().isNotFound());
    }

}
