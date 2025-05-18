package com.example.apigateway.controller;

import com.example.apigateway.dto.ComposedSupplierInventoryResponse;
import com.example.apigateway.dto.InventoryResponse;
import com.example.apigateway.dto.SupplierResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ComposedSupplierInventoryController {

    private final RestTemplate restTemplate;

    @GetMapping("/composed-supplier-inventory/{supplierId}")
    public ComposedSupplierInventoryResponse getComposedSupplierInventory(@PathVariable Long supplierId) {
        // Получаем ответ от supplier-service
        SupplierResponse supplier;
        try {
            supplier = getSupplier(supplierId);
        } catch (Exception e) {
            log.error("Error getting supplier: {}", e.getMessage());
            supplier = supplierFallback(supplierId, e);
        }

        // Получаем ответ от inventory-service
        InventoryResponse inventory;
        try {
            inventory = getInventory(supplierId);
        } catch (Exception e) {
            log.error("Error getting inventory: {}", e.getMessage());
            inventory = inventoryFallback(supplierId, e);
        }

        // Создаем комбинированный ответ
        return new ComposedSupplierInventoryResponse(supplier, inventory);
    }

    @CircuitBreaker(name = "supplierService", fallbackMethod = "supplierFallback")
    @Retry(name = "supplierService")
    public SupplierResponse getSupplier(Long supplierId) {
        log.info("Calling supplier service for supplierId: {}", supplierId);
        return restTemplate.getForObject("http://supplier-service/suppliers/" + supplierId, SupplierResponse.class);
    }

    public SupplierResponse supplierFallback(Long supplierId, Throwable t) {
        log.warn("Supplier service fallback for supplierId: {}, reason: {}", supplierId, t.getMessage());
        return new SupplierResponse(supplierId, "Fallback Supplier", 0.0, "Unknown");
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "inventoryFallback")
    @Retry(name = "inventoryService")
    public InventoryResponse getInventory(Long supplierId) {
        log.info("Calling inventory service for supplierId: {}", supplierId);
        return restTemplate.getForObject("http://inventory-service/api/inventory/items/" + supplierId, InventoryResponse.class);
    }

    public InventoryResponse inventoryFallback(Long supplierId, Throwable t) {
        log.warn("Inventory service fallback for supplierId: {}, reason: {}", supplierId, t.getMessage());
        return new InventoryResponse(supplierId, "Fallback Item", 0, 0.0);
    }
}