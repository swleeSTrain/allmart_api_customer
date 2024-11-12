package org.sunbong.allmart_api.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.payment.domain.Payment;
import org.sunbong.allmart_api.payment.domain.PaymentMethod;
import org.sunbong.allmart_api.payment.dto.PaymentDTO;
import org.sunbong.allmart_api.payment.repository.PaymentRepository;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.repository.OrderJpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderJpaRepository orderJpaRepository;

    @Transactional(readOnly = true)
    public List<PaymentDTO> findPaymentsWithOrderById(Long orderID) {
        List<Payment> payments = paymentRepository.findPaymentsWithOrderById(orderID);
        return payments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentDTO createPayment(Long orderID, String method, BigDecimal amount) {
        Optional<OrderEntity> orderEntityOptional = orderJpaRepository.findById(orderID);
        if (orderEntityOptional.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderID);
        }

        Payment payment = Payment.builder()
                .order(orderEntityOptional.get())
                .method(PaymentMethod.valueOf(method))
                .amount(amount)
                .build();

        paymentRepository.save(payment);
        return convertToDTO(payment);
    }

    @Transactional
    public PaymentDTO updatePayment(Long paymentID, int completed) {
        Payment payment = paymentRepository.findById(paymentID)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + paymentID));

        Payment updatedPayment = payment.toBuilder()
                .completed(completed)
                .build();

        paymentRepository.save(updatedPayment);
        return convertToDTO(updatedPayment);
    }

    private PaymentDTO convertToDTO(Payment payment) {
        return PaymentDTO.builder()
                .paymentID(payment.getPaymentID())
                .orderID(payment.getOrder().getOrderID())
                .method(payment.getMethod().name())
                .amount(payment.getAmount())
                .completed(payment.getCompleted())
                .build();
    }
}
