package org.sunbong.allmart_api.payment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.util.ReflectionTestUtils;
import org.sunbong.allmart_api.payment.domain.Payment;
import org.sunbong.allmart_api.payment.domain.PaymentMethod;
import org.sunbong.allmart_api.payment.dto.PaymentDTO;
import org.sunbong.allmart_api.payment.repository.PaymentRepository;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // 각 테스트 후 롤백 처리
class PaymentServiceTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        // 필요한 초기 설정이 있다면 여기에 추가
    }

    @Test
    void testFindPaymentsWithOrderById() {
        Long orderID = 1L;
        OrderEntity order = new OrderEntity();
        ReflectionTestUtils.setField(order, "orderID", orderID);
        orderRepository.save(order); // 실제로 데이터베이스에 저장

        Payment payment = Payment.builder()
                .order(order)
                .method(PaymentMethod.ONLINE_PAYMENT)
                .amount(new BigDecimal("100.00"))
                .completed(0)
                .build();

        paymentRepository.save(payment); // 실제로 데이터베이스에 저장

        List<PaymentDTO> result = paymentService.findPaymentsWithOrderById(orderID);

        assertEquals(1, result.size());
        assertEquals(orderID, result.get(0).getOrderID());
        assertEquals("ONLINE_PAYMENT", result.get(0).getMethod());
    }

    @Test
    void testCreatePaymentWithAutoIncrementedId() {
        // 새로운 주문 생성
        OrderEntity order = new OrderEntity();
        orderRepository.save(order);

        // 결제 생성
        Payment payment = Payment.builder()
                .order(order)
                .method(PaymentMethod.ONLINE_PAYMENT)
                .amount(new BigDecimal("100.00"))
                .build();

        paymentRepository.save(payment);

        // 결과 검증
        assertNotNull(payment.getPaymentID()); // paymentID가 자동 증가되었는지 확인
        assertEquals(PaymentMethod.ONLINE_PAYMENT, payment.getMethod());
        assertEquals(0, payment.getCompleted()); // 기본값이 0인지 확인
    }

    @Test
    void testCreatePayment() {
        Long orderID = 1L;
        OrderEntity order = new OrderEntity();
        ReflectionTestUtils.setField(order, "orderID", orderID);
        orderRepository.save(order); // 실제로 데이터베이스에 저장

        PaymentDTO result = paymentService.createPayment(orderID, "ONLINE_PAYMENT", new BigDecimal("200.00"));

        assertNotNull(result);
        assertEquals(orderID, result.getOrderID());
        assertEquals("ONLINE_PAYMENT", result.getMethod());
        assertEquals(new BigDecimal("200.00"), result.getAmount());
    }
}
