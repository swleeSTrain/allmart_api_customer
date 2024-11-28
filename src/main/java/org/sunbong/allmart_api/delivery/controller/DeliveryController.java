package org.sunbong.allmart_api.delivery.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.delivery.dto.DeliveryDTO;
import org.sunbong.allmart_api.delivery.service.DeliveryService;
import org.sunbong.allmart_api.order.dto.OrderDTO;
import org.sunbong.allmart_api.order.dto.OrderListDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Validated  // 클래스 전체에 유효성 검사 적용
public class DeliveryController {

    private final DeliveryService deliveryService;

    /**
     * 관리자가 특정 시간 범위 동안의 주문 데이터를 처리하여 배달 작업을 생성해야 할 때 사용
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping("/process")
    public ResponseEntity<List<DeliveryDTO>> processDelivery(@RequestParam LocalDateTime startTime,
                                                             @RequestParam LocalDateTime endTime) {
        return ResponseEntity.ok(deliveryService.processOrdersForDelivery(startTime, endTime));
    }

    /**
     * 관리자가 특정 배달 작업에 포함된 주문 목록을 조회해야 할 때 사용
     * @param deliveryId
     * @return
     */
    @GetMapping("/{deliveryId}/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersByDelivery(@PathVariable Long deliveryId) {
        return ResponseEntity.ok(deliveryService.getOrdersByDeliveryId(deliveryId));
    }

    /**
     * 관리자가 배달 상태를 PENDING → IN_PROGRESS 또는 COMPLETED로 변경
     * @param deliveryId
     * @param status 배달 상태
     * @return
     */
    @PutMapping("/{deliveryId}/status")
    public ResponseEntity<DeliveryDTO> changeDeliveryStatus(@PathVariable Long deliveryId,
                                                            @RequestParam String status) {
        return ResponseEntity.ok(deliveryService.changeDeliveryStatus(deliveryId, status));
    }

    /**
     * 대시보드 시각화
     * @return
     */
    @GetMapping("/status-count")
    public ResponseEntity<Map<String, Long>> getDeliveryStatusCount() {
        return ResponseEntity.ok(deliveryService
                .getDeliveryStatusCount());
    }

    /**
     * 상태별 리스트 보여주기
     * @param status 배달 상태
     * @return
     */
    @GetMapping("/orders-by-status")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@RequestParam String status) {
        return ResponseEntity.ok(deliveryService.getOrdersByStatus(status));
    }

}

