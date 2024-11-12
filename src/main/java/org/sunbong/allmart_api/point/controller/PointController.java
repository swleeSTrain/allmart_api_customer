package org.sunbong.allmart_api.point.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.point.dto.PointDTO;
import org.sunbong.allmart_api.point.service.PointService;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    // 특정 사용자 포인트 조회
    @GetMapping("/{userID}")
    public ResponseEntity<PointDTO> getPointsByUserID(@PathVariable Long userID) {
        PointDTO pointDTO = pointService.getPointByUserID(userID);
        return ResponseEntity.ok(pointDTO);
    }

    // 포인트 추가
    @PostMapping("/{userID}/add")
    public ResponseEntity<Void> addPoints(@PathVariable Long userID, @RequestParam Integer pointsToAdd) {
        pointService.addPoints(userID, pointsToAdd);
        return ResponseEntity.ok().build();
    }

    // 포인트 차감
    @PostMapping("/{userID}/deduct")
    public ResponseEntity<Void> deductPoints(@PathVariable Long userID, @RequestParam Integer pointsToDeduct) {
        pointService.deductPoints(userID, pointsToDeduct);
        return ResponseEntity.ok().build();
    }
}
