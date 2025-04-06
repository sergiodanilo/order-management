package com.project.test.ordermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resale_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResaleOrder {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false)
    private UUID orderNumber;

    @Column(unique = true, nullable = false)
    private String orderIds;

    @Column(nullable = false)
    private LocalDateTime orderDate;

}
