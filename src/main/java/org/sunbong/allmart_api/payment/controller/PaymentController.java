package org.sunbong.allmart_api.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.payment.dto.PaymentDTO;
import org.sunbong.allmart_api.payment.domain.Payment;
import org.sunbong.allmart_api.payment.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 생성
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            // Payment 객체 생성
            Payment payment = paymentService.createPayment(paymentDTO);

            // Payment 객체를 PaymentDTO로 변환하여 응답
            PaymentDTO responseDTO = PaymentDTO.builder()
                    .paymentID(payment.getPaymentID())
                    .orderID(payment.getOrder().getOrderID())
                    .method(payment.getMethod().name())
                    .amount(payment.getAmount())
                    .completed(payment.getCompleted())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            // 에러 메시지를 자세히 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
