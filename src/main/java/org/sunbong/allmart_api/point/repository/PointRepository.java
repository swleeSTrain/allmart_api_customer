package org.sunbong.allmart_api.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.point.domain.Point;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByCustomerID(Long customerID); // 사용자 ID로 조회
}
