package org.sunbong.allmart_api.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.repository.OrderRepository;
import org.sunbong.allmart_api.payment.domain.Payment;
import org.sunbong.allmart_api.payment.domain.PaymentMethod;
import org.sunbong.allmart_api.payment.dto.PaymentDTO;
import org.sunbong.allmart_api.payment.repository.PaymentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Payment createPayment(PaymentDTO paymentDTO) {
        // PaymentDTO에서 orderID를 가져와 OrderEntity 조회
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

    public Optional<PaymentDTO> getPaymentById(Long paymentID) {
        return paymentRepository.findById(paymentID)
                .map(this::convertToDTO);
    }

    @Transactional
    public PaymentDTO completePayment(Long paymentID) {
        Payment payment = paymentRepository.findById(paymentID)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment = payment.toBuilder().completed(1).build();
        paymentRepository.save(payment);
        return convertToDTO(payment);
    }

    private PaymentDTO convertToDTO(Payment payment) {
        return PaymentDTO.builder()
                .paymentID(payment.getPaymentID())
                .orderID(payment.getOrder().getOrderID()) // OrderEntity의 orderID 참조
                .method(payment.getMethod().name())
                .amount(payment.getAmount())
                .completed(payment.getCompleted())
                .build();
    }
}
