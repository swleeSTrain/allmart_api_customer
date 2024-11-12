package org.sunbong.allmart_api.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sunbong.allmart_api.product.dto.ProductSummaryDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderListDTO {
    private Long orderId;
    private String customerName;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
}