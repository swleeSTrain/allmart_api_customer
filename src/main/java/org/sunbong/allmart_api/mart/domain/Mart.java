package org.sunbong.allmart_api.mart.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sunbong.allmart_api.common.domain.BaseEntity;

@Entity
@Table(name = "tbl_mart")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Mart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "martID")
    private Long martID; // 마트ID (기본키)

    @Column(name = "martName", length = 50, nullable = false)
    private String martName; // 이름

    @Column(name = "phoneNumber", length = 13, nullable = false)
    private String phoneNumber; // 전화번호

    @Column(name = "logo", nullable = false)
    private String logo; // 마트로고

    @Column(name = "template", length = 20, nullable = false)
    private String template; // 템플릿

    @Column(name = "address", length = 500, nullable = false)
    private String address; // 주소

    @Column(name = "certificate", length = 255, nullable = false)
    private String certificate; // 사업자등록증
}