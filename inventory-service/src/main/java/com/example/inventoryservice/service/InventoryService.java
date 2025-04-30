package com.example.inventoryservice.service;

import com.example.inventoryservice.model.InventoryItem;
import com.example.inventoryservice.model.Order;
import com.example.inventoryservice.repository.InventoryItemRepository;
import com.example.inventoryservice.repository.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final OrderRepository orderRepository;

    public InventoryService(InventoryItemRepository inventoryItemRepository, OrderRepository orderRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.orderRepository = orderRepository;
    }

    public List<InventoryItem> getAllInventoryItems() {
        return inventoryItemRepository.findAll();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
