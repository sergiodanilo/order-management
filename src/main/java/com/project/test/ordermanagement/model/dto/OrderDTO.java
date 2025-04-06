package com.project.test.ordermanagement.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.test.ordermanagement.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    private Long orderNumber;

    @NotNull
    private String taxId;

    private String client;
    private Set<OrderItemDTO> orderItems;
    private LocalDate date;
    private BigDecimal totalAmount;
    private OrderStatus status;

}
