package org.sunbong.allmart_api.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderCheckDTO {
    private Long orderID;
    private String customerName;
    private String phoneNumber; // 고객 전화번호
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
    // 기본값으로 빈 리스트를 설정하여 NullPointerException 방지
    @Builder.Default
    private List<OrderItemDTO> orderItems = new ArrayList<>();
}
