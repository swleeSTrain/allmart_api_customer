package org.sunbong.allmart_api.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.payment.domain.Payment;
import org.sunbong.allmart_api.payment.domain.PaymentMethod;
import org.sunbong.allmart_api.payment.dto.PaymentDTO;
import org.sunbong.allmart_api.payment.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment payment = Payment.builder()
                .orderID(paymentDTO.getOrderID())
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
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentID);

        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get().toBuilder()
                    .orderID(paymentDTO.getOrderID())
                    .method(Enum.valueOf(PaymentMethod.class, paymentDTO.getMethod()))
                    .amount(paymentDTO.getAmount())
                    .completed(paymentDTO.getCompleted())
                    .serial(paymentDTO.getSerial())
                    .build();

            Payment updatedPayment = paymentRepository.save(payment);
            return toDTO(updatedPayment);
        }

        return null;
    }

    public void deletePayment(Long paymentID) {
        paymentRepository.deleteById(paymentID);
    }

    // 모든 Payment 조회
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    private PaymentDTO toDTO(Payment payment) {
        return PaymentDTO.builder()
                .paymentID(payment.getPaymentID())
                .orderID(payment.getOrderID())
                .method(payment.getMethod().name())
                .amount(payment.getAmount())
                .completed(payment.getCompleted())
                .serial(payment.getSerial())
                .build();
    }
}
