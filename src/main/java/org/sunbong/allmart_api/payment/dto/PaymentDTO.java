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
    private long orderID;  // OrderEntity의 orderID를 참조
    private String method;
    private BigDecimal amount;
    private int completed;
}
