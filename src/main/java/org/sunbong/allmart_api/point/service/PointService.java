package org.sunbong.allmart_api.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.point.domain.Point;
import org.sunbong.allmart_api.point.dto.PointDTO;
import org.sunbong.allmart_api.point.repository.PointRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    // 모든 사용자 포인트 조회
    public List<PointDTO> getAllPoints() {
        return pointRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PointDTO getPointByUserID(Long customerID) {
        Optional<Point> point = pointRepository.findByCustomerID(customerID); // 수정된 부분
        return point.map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Point not found for customerID: " + customerID));
    }

    public void addPoints(Long customerID, Integer pointsToAdd) {
        Point point = pointRepository.findByCustomerID(customerID) // 수정된 부분
                .orElse(Point.builder()
                        .customerID(customerID)
                        .totalPoints(0)
                        .build());
        point = Point.builder()
                .pointID(point.getPointID())
                .customerID(point.getCustomerID())
                .totalPoints(point.getTotalPoints() + pointsToAdd)
                .build();
        pointRepository.save(point);
    }

    public void deductPoints(Long customerID, Integer pointsToDeduct) {
        Point point = pointRepository.findByCustomerID(customerID) // 수정된 부분
                .orElseThrow(() -> new IllegalArgumentException("Point not found for customerID: " + customerID));
        if (point.getTotalPoints() < pointsToDeduct) {
            throw new IllegalArgumentException("Insufficient points for customerID: " + customerID);
        }
        point = Point.builder()
                .pointID(point.getPointID())
                .customerID(point.getCustomerID())
                .totalPoints(point.getTotalPoints() - pointsToDeduct)
                .build();
        pointRepository.save(point);
    }

    private PointDTO convertToDTO(Point point) {
        return PointDTO.builder()
                .customerID(point.getCustomerID())
                .totalPoints(point.getTotalPoints())
                .build();
    }
}
