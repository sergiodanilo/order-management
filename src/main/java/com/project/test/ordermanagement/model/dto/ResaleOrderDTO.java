package com.project.test.ordermanagement.model.dto;

import com.project.test.ordermanagement.model.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ResaleOrderDTO {

    private UUID orderNumber;
    private List<Order> orders;

}
