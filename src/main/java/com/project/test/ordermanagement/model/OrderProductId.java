package com.project.test.ordermanagement.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class OrderProductId implements Serializable {

    private Long orderId;
    private Long productId;

}
