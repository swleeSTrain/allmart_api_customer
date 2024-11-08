package org.sunbong.allmart_api.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sunbong.allmart_api.product.dto.ProductSummaryDTO;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private String orderStatus;
    private BigDecimal unitPrice;
    private short quantity;
    private String productName;
}