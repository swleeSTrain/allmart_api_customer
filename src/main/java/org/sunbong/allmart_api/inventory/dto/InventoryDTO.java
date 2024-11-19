package org.sunbong.allmart_api.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private Long inventoryID;
    private String sku;
    private Integer quantity;
    private Integer inStock;
}

