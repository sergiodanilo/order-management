package com.project.test.ordermanagement.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDTO {

    private Long productId;

    private String product;

    private Integer quantity;

    private BigDecimal price;

}
