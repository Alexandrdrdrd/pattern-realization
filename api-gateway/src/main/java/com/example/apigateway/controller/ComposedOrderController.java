package com.example.apigateway.controller;

import com.example.apigateway.dto.ComposedOrderResponse;
import com.example.apigateway.dto.InventoryResponse;
import com.example.apigateway.dto.OrderResponse;
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
public class ComposedOrderController {

    private final RestTemplate restTemplate;

    @GetMapping("/composed-order/{orderId}")
    public ComposedOrderResponse getComposedOrder(@PathVariable Long orderId) {
        // Получаем ответ от order-service
        OrderResponse order;
        try {
            order = getOrder(orderId);
        } catch (Exception e) {
            log.error("Error getting order: {}", e.getMessage());
            order = orderFallback(orderId, e);
        }

        // Получаем ответ от inventory-service
        InventoryResponse inventory;
        try {
            inventory = getInventory(orderId);
        } catch (Exception e) {
            log.error("Error getting inventory: {}", e.getMessage());
            inventory = inventoryFallback(orderId, e);
        }

        // Создаем комбинированный ответ
        return new ComposedOrderResponse(order, inventory);
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "orderFallback")
    @Retry(name = "orderService")
    public OrderResponse getOrder(Long orderId) {
        log.info("Calling order service for orderId: {}", orderId);
        return restTemplate.getForObject("http://order-service/orders/" + orderId, OrderResponse.class);
    }

    public OrderResponse orderFallback(Long orderId, Throwable t) {
        log.warn("Order service fallback for orderId: {}, reason: {}", orderId, t.getMessage());
        return new OrderResponse(orderId, "Fallback Order", 0.0, "Unknown");
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "inventoryFallback")
    @Retry(name = "inventoryService")
    public InventoryResponse getInventory(Long orderId) {
        log.info("Calling inventory service for orderId: {}", orderId);
        return restTemplate.getForObject("http://inventory-service/api/inventory/items/" + orderId, InventoryResponse.class);
    }

    public InventoryResponse inventoryFallback(Long orderId, Throwable t) {
        log.warn("Inventory service fallback for orderId: {}, reason: {}", orderId, t.getMessage());
        return new InventoryResponse(orderId, "Fallback Item", 0, 0.0);
    }
}