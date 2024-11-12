package org.sunbong.allmart_api.point.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_point")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointID;

    @Column(nullable = false)
    private Long customerID; // 사용자 ID와 매핑

    @Column(nullable = false)
    private Integer totalPoints; // 총 포인트
}
