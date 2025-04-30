package com.example.apigateway.dto;

public record OrderResponse(Long id, String customer, Double amount, String createdAt) {}
