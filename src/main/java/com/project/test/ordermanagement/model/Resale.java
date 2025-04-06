package com.project.test.ordermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resale")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resale {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(length = 14, unique = true, nullable = false)
    private String taxId;

    @NotNull
    @Column(length = 100, nullable = false)
    private String businessName;

    @NotNull
    @Column(length = 100, nullable = false)
    private String tradeName;

    @NotNull
    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 20)
    private String phone;

    @NotNull
    @Column(length = 100, nullable = false)
    private String contractName;

    @NotNull
    @Column(nullable = false)
    private String deliveryAddress;

}