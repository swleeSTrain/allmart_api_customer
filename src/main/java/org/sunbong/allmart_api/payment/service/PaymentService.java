package org.sunbong.allmart_api.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.repository.OrderJpaRepository;
import org.sunbong.allmart_api.payment.domain.Payment;
import org.sunbong.allmart_api.payment.domain.PaymentMethod;
import org.sunbong.allmart_api.payment.dto.PaymentDTO;
import org.sunbong.allmart_api.payment.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderJpaRepository orderRepository;

    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        OrderEntity order = orderRepository.findById(paymentDTO.getOrderID())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + paymentDTO.getOrderID()));

        Payment payment = Payment.builder()
                .order(order) // OrderEntity 객체 설정
                .method(Enum.valueOf(PaymentMethod.class, paymentDTO.getMethod()))
                .amount(paymentDTO.getAmount())
                .completed(paymentDTO.getCompleted())
                .serial(paymentDTO.getSerial())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return toDTO(savedPayment);
    }

    public PaymentDTO getPaymentById(Long paymentID) {
        Optional<Payment> payment = paymentRepository.findById(paymentID);
        return payment.map(this::toDTO).orElse(null);
    }

    public PaymentDTO updatePayment(Long paymentID, PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.findById(paymentID)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + paymentID));

        OrderEntity order = orderRepository.findById(paymentDTO.getOrderID())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + paymentDTO.getOrderID()));

        // 새로운 Payment 객체를 빌드하여 업데이트
        Payment updatedPayment = payment.toBuilder()
                .order(order)
                .method(Enum.valueOf(PaymentMethod.class, paymentDTO.getMethod()))
                .amount(paymentDTO.getAmount())
                .completed(paymentDTO.getCompleted())
                .serial(paymentDTO.getSerial())
                .build();

        Payment savedPayment = paymentRepository.save(updatedPayment);
        return toDTO(savedPayment);
    }

    public void deletePayment(Long paymentID) {
        paymentRepository.deleteById(paymentID);
    }

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    private PaymentDTO toDTO(Payment payment) {
        return PaymentDTO.builder()
                .paymentID(payment.getPaymentID())
                .orderID(payment.getOrder().getOrderID()) // OrderEntity에서 orderID 가져오기
                .method(payment.getMethod().name())
                .amount(payment.getAmount())
                .completed(payment.getCompleted())
                .serial(payment.getSerial())
                .build();
    }
}
