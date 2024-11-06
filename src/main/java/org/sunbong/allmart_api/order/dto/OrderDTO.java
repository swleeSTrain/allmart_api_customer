package org.sunbong.allmart_api.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private Long orderID;
    private String customerName;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
}