package com.example.apigateway.dto;

public record ComposedSupplierInventoryResponse(SupplierResponse supplier, InventoryResponse inventory) {}