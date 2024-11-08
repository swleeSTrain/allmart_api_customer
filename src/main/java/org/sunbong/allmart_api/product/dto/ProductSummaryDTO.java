package org.sunbong.allmart_api.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSummaryDTO {
    private Long productId;
    private String productName;
    private int quantity;
}