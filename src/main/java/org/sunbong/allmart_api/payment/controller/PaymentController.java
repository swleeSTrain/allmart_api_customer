package org.sunbong.allmart_api.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.payment.dto.PaymentDTO;
import org.sunbong.allmart_api.payment.domain.Payment;
import org.sunbong.allmart_api.payment.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // 결제 생성
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        Payment payment = paymentService.createPayment(paymentDTO);
        PaymentDTO responseDTO = new PaymentDTO(
                payment.getPaymentID(),
                payment.getOrder().getOrderID(),
                payment.getMethod().name(),
                payment.getAmount(),
                payment.getCompleted()
        );
        return ResponseEntity.ok(responseDTO);
    }

    // 특정 결제 조회
    @GetMapping("/{paymentID}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long paymentID) {
        return paymentService.getPaymentById(paymentID)
                .map(payment -> new PaymentDTO(
                        payment.getPaymentID(),
                        payment.getOrder().getOrderID(),
                        payment.getMethod().name(),
                        payment.getAmount(),
                        payment.getCompleted()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 결제 완료 처리
    @PutMapping("/{paymentID}/complete")
    public ResponseEntity<PaymentDTO> completePayment(@PathVariable Long paymentID) {
        try {
            Payment payment = paymentService.completePayment(paymentID);
            PaymentDTO responseDTO = new PaymentDTO(
                    payment.getPaymentID(),
                    payment.getOrder().getOrderID(),
                    payment.getMethod().name(),
                    payment.getAmount(),
                    payment.getCompleted()
            );
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
