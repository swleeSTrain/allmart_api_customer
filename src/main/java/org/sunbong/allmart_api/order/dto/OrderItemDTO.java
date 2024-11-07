package org.sunbong.allmart_api.order.dto;// OrderItemDTO.java
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sunbong.allmart_api.product.domain.ProductImage;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private short quantity;
    private BigDecimal unitPrice;
    private List<ProductImage> attachFiles;  // ProductImage 리스트를 필드로 추가
}
