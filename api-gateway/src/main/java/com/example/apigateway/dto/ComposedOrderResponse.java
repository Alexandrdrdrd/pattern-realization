package com.example.apigateway.dto;

public record ComposedOrderResponse(OrderResponse order, InventoryResponse inventory) {}
