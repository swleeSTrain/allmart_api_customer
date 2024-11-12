package org.sunbong.allmart_api.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long paymentID;
    private Long orderID;
    private String method;
    private BigDecimal amount;
    private int completed;
    private String serial;  // Payment 엔티티의 serial 필드
}