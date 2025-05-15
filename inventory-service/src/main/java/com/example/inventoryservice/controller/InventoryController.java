package com.example.inventoryservice.controller;

import com.example.inventoryservice.model.InventoryItem;

import com.example.inventoryservice.repository.InventoryItemRepository;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryController(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @GetMapping("/items")
    public List<InventoryItem> getAllInventoryItems() {
        return inventoryItemRepository.findAll();
    }

    @PostMapping("/items")
    public InventoryItem saveInventoryItems(@RequestBody InventoryItem inventoryItem) {
        return inventoryItemRepository.save(inventoryItem);
    }

    @GetMapping("/items/{id}")
    public Optional<InventoryItem> getInventoryItemById(@PathVariable Long id) {
        return inventoryItemRepository.findById(id);
    }

}
