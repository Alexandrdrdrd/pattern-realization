package com.example.apigateway.controller;

import com.example.apigateway.dto.ComposedOrderResponse;
import com.example.apigateway.dto.InventoryResponse;
import com.example.apigateway.dto.OrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ComposedOrderController {

    private final RestTemplate restTemplate;

    @GetMapping("/composed-order/{orderId}")
    public ComposedOrderResponse getComposedOrder(@PathVariable Long orderId) {
        OrderResponse order = getOrder(orderId);
        InventoryResponse inventory = getInventory(orderId);
        return new ComposedOrderResponse(order, inventory);
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "orderFallback")
    public OrderResponse getOrder(Long orderId) {
        return restTemplate.getForObject("http://order-service/api/orders/" + orderId, OrderResponse.class);
    }

    public OrderResponse orderFallback(Long orderId, Throwable t) {
        return new OrderResponse(orderId, "Fallback Order", 0.0, "Unknown");
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "inventoryFallback")
    public InventoryResponse getInventory(Long orderId) {
        return restTemplate.getForObject("http://inventory-service/api/inventory/" + orderId, InventoryResponse.class);
    }

    public InventoryResponse inventoryFallback(Long orderId, Throwable t) {
        return new InventoryResponse(orderId, false, 0);
    }
}
