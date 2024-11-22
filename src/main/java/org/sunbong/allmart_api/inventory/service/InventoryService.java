package org.sunbong.allmart_api.inventory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.inventory.domain.Inventory;
import org.sunbong.allmart_api.inventory.dto.InventoryDTO;
import org.sunbong.allmart_api.inventory.dto.InventoryListDTO;
import org.sunbong.allmart_api.inventory.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public PageResponseDTO<InventoryListDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("InventoryService - list called with PageRequestDTO: {}", pageRequestDTO);

        return inventoryRepository.searchInventory(pageRequestDTO);
    }


//    /**
//     * 검색 및 페이징 처리된 재고 리스트 반환
//     */
//    public PageResponseDTO<InventoryDTO> searchInventory(PageRequestDTO pageRequestDTO) {
//        return inventoryRepository.searchInventory(pageRequestDTO);
//    }
//
//    /**
//     * 전체 재고 리스트 반환 (페이징 없이)
//     */
//    public List<InventoryDTO> getAllInventories() {
//        return inventoryRepository.findAll().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * ID로 특정 재고 조회
//     */
//    public Optional<InventoryDTO> getInventoryById(Long id) {
//        return inventoryRepository.findById(id).map(this::convertToDTO);
//    }

//    /**
//     * 새로운 재고 생성
//     */
//    public Long createInventory(InventoryDTO inventoryDTO) {
//        Inventory inventory = convertToEntity(inventoryDTO);
//        inventory = inventoryRepository.save(inventory);
//        return inventory.getInventoryID();
//    }
//
//    /**
//     * 재고 수정
//     */
//    public InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) {
//        Inventory inventory = inventoryRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Inventory not found"));
//
//        // 기존 엔티티를 기반으로 새로운 객체 생성
//        Inventory updatedInventory = inventory.toBuilder()
//                .sku(inventoryDTO.getSku())
//                .quantity(inventoryDTO.getQuantity())
//                .inStock(inventoryDTO.getInStock())
//                .build();
//
//        // 수정된 객체 저장
//        inventoryRepository.save(updatedInventory);
//        return convertToDTO(updatedInventory);
//    }
//
//    /**
//     * 재고 삭제
//     */
//    public void deleteInventory(Long id) {
//        inventoryRepository.deleteById(id);
//    }
//
//    private InventoryDTO convertToDTO(Inventory inventory) {
//        return InventoryDTO.builder()
//                .inventoryID(inventory.getInventoryID())
//                .sku(inventory.getSku())
//                .quantity(inventory.getQuantity())
//                .inStock(inventory.getInStock())
//                .build();
//    }
//
//    private Inventory convertToEntity(InventoryDTO inventoryDTO) {
//        return Inventory.builder()
//                .sku(inventoryDTO.getSku())
//                .quantity(inventoryDTO.getQuantity())
//                .inStock(inventoryDTO.getInStock())
//                .build();
//    }
}
