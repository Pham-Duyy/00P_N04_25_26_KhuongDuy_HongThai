package com.fund.group09.quanlyngansach.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fund.group09.quanlyngansach.Model.Inventory;
import com.fund.group09.quanlyngansach.Repository.InventoryRepository;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    public void updateInventory(Long id, Inventory inventoryDetails) {
        Inventory inventory = inventoryRepository.findById(id).orElse(null);
        if (inventory != null) {
            inventory.setStockLevel(inventoryDetails.getStockLevel());
            inventory.setProduct(inventoryDetails.getProduct());
            inventoryRepository.save(inventory);
        }
    }
}