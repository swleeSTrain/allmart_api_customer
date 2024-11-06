package org.sunbong.allmart_api.payment.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.repository.OrderRepository;
import org.sunbong.allmart_api.payment.domain.Payment;
import org.sunbong.allmart_api.payment.domain.PaymentMethod;
import org.sunbong.allmart_api.payment.dto.PaymentDTO;
import org.sunbong.allmart_api.payment.repository.PaymentRepository;


import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Payment createPayment(PaymentDTO paymentDTO) {
        // OrderEntity를 orderID로 조회
        OrderEntity order = orderRepository.findById(paymentDTO.getOrderID())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 조회된 OrderEntity를 사용하여 Payment 객체 생성
        Payment payment = Payment.builder()
                .order(order)
                .method(PaymentMethod.valueOf(paymentDTO.getMethod()))
                .amount(paymentDTO.getAmount())
                .completed(paymentDTO.getCompleted())
                .build();

        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentById(Long paymentID) {
        return paymentRepository.findById(paymentID);
    }

    @Transactional
    public Payment completePayment(Long paymentID) {
        Payment payment = paymentRepository.findById(paymentID)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment = payment.toBuilder().completed(1).build(); // 새로운 Payment 인스턴스 생성
        return paymentRepository.save(payment);
    }
}
