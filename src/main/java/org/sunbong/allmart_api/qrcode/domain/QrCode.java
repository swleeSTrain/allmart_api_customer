package org.sunbong.allmart_api.qrcode.domain;


import jakarta.persistence.*;
import lombok.*;
import org.sunbong.allmart_api.customer.domain.Customer;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "customer")
@Getter
public class QrCode {

    @Id
    @Column(length = 40, nullable = false)
    private String fileName;

    //QR 코드에 포함할 실제 정보
    @Column(nullable = false)
    private String data;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime expireTime;

    @Column(nullable = false)
    private String qrCodeURL;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isExpired;

    @Column(nullable = false)
    @Enumerated
    private QrCodeType qrCodeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerID", referencedColumnName = "customerID", nullable = true)
    private Customer customer;




}