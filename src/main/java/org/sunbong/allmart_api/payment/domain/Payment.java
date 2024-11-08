package org.sunbong.allmart_api.payment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sunbong.allmart_api.common.domain.BaseEntity;
import org.sunbong.allmart_api.order.domain.OrderEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_payment")
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentID", nullable = false, updatable = false)
    private Long paymentID;

    @OneToOne
    @JoinColumn(name = "orderID", nullable = false, columnDefinition = "BIGINT")
    private OrderEntity order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount; // BigDecimal을 그대로 사용

    @Column(nullable = false)
    @Builder.Default
    private int completed = 0;

}
