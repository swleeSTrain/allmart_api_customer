package org.sunbong.allmart_api.mart.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_martLogo") // 테이블명
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MartLogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logoID")
    private Long logoID; // 마트ID (기본키)

    @Column(name = "logoURL", length = 255, nullable = false)
    private String logoURL; // 로고 URL

}