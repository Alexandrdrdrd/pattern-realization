package com.example.apigateway.dto;

public record InventoryResponse(Long orderId, boolean inStock, int availableQuantity) {}
