package com.project.test.ordermanagement.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class ResaleOrderDTO {

    private UUID orderNumber;
    private Set<OrderDTO> orders;

}
