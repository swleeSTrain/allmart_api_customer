package org.sunbong.allmart_api.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {

    private Long productID;
    private String name;
    private String sku;
    private BigDecimal price;

    private String thumbnailImage; // 썸네일 이미지 URL
}
