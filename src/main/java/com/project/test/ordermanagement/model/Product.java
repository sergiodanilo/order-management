package com.project.test.ordermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false)
    private String sku;

    @NotNull
    @Column(length = 100, nullable = false)
    private String name;

    private String description;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(length = 100, nullable = false)
    private String measureUnit;

}