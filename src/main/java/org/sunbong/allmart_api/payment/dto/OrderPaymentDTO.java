package org.sunbong.allmart_api.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sunbong.allmart_api.payment.domain.PaymentMethod;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentDTO {
    private long paymentID;
    private PaymentMethod method;
    private BigDecimal amount;
    private int completed;
}
