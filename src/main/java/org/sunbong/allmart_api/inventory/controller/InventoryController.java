package org.sunbong.allmart_api.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.inventory.dto.InventoryDTO;
import org.sunbong.allmart_api.inventory.dto.InventoryListDTO;
import org.sunbong.allmart_api.inventory.service.InventoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<InventoryListDTO>> list(PageRequestDTO pageRequestDTO) {
        PageResponseDTO<InventoryListDTO> response = inventoryService.list(pageRequestDTO);
        return ResponseEntity.ok(response);
    }

//    /**
//     * 검색 및 페이징 처리된 재고 리스트 반환
//     */
//    @GetMapping
//    public ResponseEntity<PageResponseDTO<InventoryDTO>> searchInventory(PageRequestDTO pageRequestDTO) {
//        PageResponseDTO<InventoryDTO> response = inventoryService.searchInventory(pageRequestDTO);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 전체 재고 리스트 반환
//     */
//    @GetMapping("/list")
//    public ResponseEntity<List<InventoryDTO>> getAllInventories() {
//        List<InventoryDTO> response = inventoryService.getAllInventories();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * ID로 특정 재고 조회
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable Long id) {
//        Optional<InventoryDTO> inventoryDTO = inventoryService.getInventoryById(id);
//        return inventoryDTO.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    /**
//     * 새로운 재고 생성
//     */
//    @PostMapping
//    public ResponseEntity<Long> createInventory(@RequestBody InventoryDTO inventoryDTO) {
//        Long inventoryId = inventoryService.createInventory(inventoryDTO);
//        return ResponseEntity.ok(inventoryId);
//    }
//
//    /**
//     * 재고 수정
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<InventoryDTO> updateInventory(
//            @PathVariable Long id,
//            @RequestBody InventoryDTO inventoryDTO) {
//        InventoryDTO updatedInventory = inventoryService.updateInventory(id, inventoryDTO);
//        return ResponseEntity.ok(updatedInventory);
//    }
//
//    /**
//     * 재고 삭제
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
//        inventoryService.deleteInventory(id);
//        return ResponseEntity.noContent().build();
//    }
}
