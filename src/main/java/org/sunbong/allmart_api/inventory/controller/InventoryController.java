package org.sunbong.allmart_api.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.inventory.dto.InventoryDTO;
import org.sunbong.allmart_api.inventory.service.InventoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // 모든 재고 조회
    @GetMapping
    public List<InventoryDTO> getAllInventories() {
        return inventoryService.getAllInventories();
    }

    // ID로 특정 재고 조회
    @GetMapping("/{id}")
    public Optional<InventoryDTO> getInventoryById(@PathVariable Long id) {
        return inventoryService.getInventoryById(id);
    }


}
