package org.sunbong.allmart_api.order.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.order.domain.OrderStatus;
import org.sunbong.allmart_api.order.dto.NaverChatbotOrderDTO;
import org.sunbong.allmart_api.order.dto.OrderListDTO;
import org.sunbong.allmart_api.order.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated  // 클래스 전체에 유효성 검사 적용
public class OrderController {

    private final OrderService orderService;

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderListDTO> getOrderById(
            @PathVariable @Positive Long orderId) { // orderId는 양수여야 함
        OrderListDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // 주문 목록 조회
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<OrderListDTO>> searchOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String customerId,
            @Validated PageRequestDTO pageRequestDTO) { // PageRequestDTO 유효성 검사
        PageResponseDTO<OrderListDTO> orders = orderService.searchOrders(status, customerId, pageRequestDTO);
        return ResponseEntity.ok(orders);
    }

    // 주문 상태 변경
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> changeOrderStatus(
            @PathVariable @Positive Long orderId, // orderId는 양수여야 함
            @RequestParam @NotNull OrderStatus newStatus) { // newStatus는 필수 값
        orderService.changeOrderStatus(orderId, newStatus);
        return ResponseEntity.noContent().build();
    }

    // 주문 생성
    @PostMapping("/voice")
    public ResponseEntity<String> createVoiceOrder(@RequestBody NaverChatbotOrderDTO naverOrderDTO) {
        String productName = null;
        int quantity = 0;

        // JSON 데이터에서 상품명과 수량 추출
        for (NaverChatbotOrderDTO.Bubble bubble : naverOrderDTO.getBubbles()) {
            for (NaverChatbotOrderDTO.Bubble.Slot slot : bubble.getSlot()) {
                if ("상품종류".equals(slot.getName())) {
                    productName = slot.getValue();
                } else if ("수량".equals(slot.getName())) {
                    try {
                        quantity = Integer.parseInt(slot.getValue());
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body("Invalid quantity format.");
                    }
                }
            }
        }

        if (productName == null || quantity <= 0) {
            return ResponseEntity.badRequest().body("Invalid product name or quantity.");
        }

        // 주문 생성 서비스 호출
        orderService.createOrderFromVoice(productName, quantity, naverOrderDTO.getUserId());
        return ResponseEntity.ok("Order created successfully.");
    }
}
