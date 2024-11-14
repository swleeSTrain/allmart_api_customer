package org.sunbong.allmart_api.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.inventory.domain.Inventory;
import org.sunbong.allmart_api.inventory.dto.InventoryDTO;
import org.sunbong.allmart_api.inventory.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    // 전체 재고 조회 (DTO 변환)
    public List<InventoryDTO> getAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ID로 재고 조회 (DTO 변환)
    public Optional<InventoryDTO> getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .map(this::convertToDTO);
    }

    private InventoryDTO convertToDTO(Inventory inventory) {
        return InventoryDTO.builder()
                .inventoryID(inventory.getInventoryID())
                .sku(inventory.getProduct().getSku())
                .quantity(inventory.getQuantity())
                .inStock(inventory.getInStock())
                .build();
    }
}
