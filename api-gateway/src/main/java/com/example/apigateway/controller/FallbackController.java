package com.example.apigateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/suppliers")  // новый endpoint
    public ResponseEntity<String> suppliersFallback() {
        return ResponseEntity.ok("Supplier Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/inventory")
    public ResponseEntity<String> inventoryFallback() {
        return ResponseEntity.ok("Inventory Service is currently unavailable. Please try again later.");
    }
}