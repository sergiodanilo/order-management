package com.project.test.ordermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.test.ordermanagement.model.dto.OrderDTO;
import com.project.test.ordermanagement.model.dto.ResaleOrderDTO;
import com.project.test.ordermanagement.model.enums.OrderStatus;
import com.project.test.ordermanagement.service.ResaleOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResaleOrderController.class)
class ResaleOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResaleOrderService resaleOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSendResaleOrders_ReturnsSuccess() throws Exception {
        List<Long> orderIds = List.of(1L, 2L);

        Set<OrderDTO> orders = Set.of(
                OrderDTO.builder()
                        .taxId("123456789")
                        .totalAmount(BigDecimal.TEN)
                        .orderNumber(1L)
                        .status(OrderStatus.DELIVERED)
                        .build(),
                OrderDTO.builder()
                        .taxId("987654321")
                        .totalAmount(BigDecimal.TEN)
                        .orderNumber(2L)
                        .status(OrderStatus.DELIVERED)
                        .build()
        );

        UUID resaleOrderNumber = UUID.randomUUID();

        ResaleOrderDTO mockResponse = ResaleOrderDTO.builder()
                .orderNumber(resaleOrderNumber)
                .orders(orders)
                .build();

        when(resaleOrderService.sendResaleOrders(orderIds)).thenReturn(mockResponse);

        mockMvc.perform(post("/api/resale-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value(resaleOrderNumber.toString()))
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders[?(@.orderNumber == 1)]").exists())
                .andExpect(jsonPath("$.orders[?(@.orderNumber == 2)]").exists());
    }

}
