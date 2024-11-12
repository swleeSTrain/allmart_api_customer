package org.sunbong.allmart_api.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.point.domain.Point;
import org.sunbong.allmart_api.point.dto.PointDTO;
import org.sunbong.allmart_api.point.repository.PointRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public PointDTO getPointByUserID(Long userID) {
        Optional<Point> point = pointRepository.findByUserID(userID);
        return point.map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Point not found for userID: " + userID));
    }

    public void addPoints(Long userID, Integer pointsToAdd) {
        Point point = pointRepository.findByUserID(userID)
                .orElse(Point.builder()
                        .customerID(userID)
                        .totalPoints(0)
                        .build());
        point = Point.builder()
                .pointID(point.getPointID())
                .customerID(point.getCustomerID())
                .totalPoints(point.getTotalPoints() + pointsToAdd)
                .build();
        pointRepository.save(point);
    }

    public void deductPoints(Long userID, Integer pointsToDeduct) {
        Point point = pointRepository.findByUserID(userID)
                .orElseThrow(() -> new IllegalArgumentException("Point not found for userID: " + userID));
        if (point.getTotalPoints() < pointsToDeduct) {
            throw new IllegalArgumentException("Insufficient points for userID: " + userID);
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
                .userID(point.getCustomerID())
                .totalPoints(point.getTotalPoints())
                .build();
    }
}
