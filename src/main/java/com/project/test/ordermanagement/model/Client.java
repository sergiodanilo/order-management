package com.project.test.ordermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(length = 100, nullable = false)
    private String name;

    @NotNull
    @Column(length = 14, unique = true, nullable = false)
    private String taxId;

    @NotNull
    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 20)
    private String phone;

    @NotNull
    @Column(nullable = false)
    private String address;

}
