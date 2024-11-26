package org.sunbong.allmart_api.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryListDTO {

    private Long inventoryID;    // 재고 ID
    private String productName;  // 상품 이름
    private String sku;          // 상품 SKU
    private Integer quantity;    // 재고 수량
    private Integer inStock;     // 재고 상태 (1: 재고 있음, 0: 재고 없음)
}
