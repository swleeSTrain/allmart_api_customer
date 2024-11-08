package org.sunbong.allmart_api.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    private String orderStatus;
    private int orderQuantity; // 주문 수량 (OrderItems 수)
    private Long customerId;
    private String paymentMethod;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
}