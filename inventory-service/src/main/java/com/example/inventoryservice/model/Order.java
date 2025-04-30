package com.example.inventoryservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    private Long id;

    private String customer;

    private Double amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
