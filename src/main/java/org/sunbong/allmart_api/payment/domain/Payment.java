package org.sunbong.allmart_api.payment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sunbong.allmart_api.common.domain.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_payment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private long paymentID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount; // BigDecimal을 그대로 사용

    @Column(nullable = false, columnDefinition = "int default 0") // 기본값을 명확히 설정하기 위해
    @Builder.Default
    private int completed = 0; // 기본값을 0으로 설정
}
