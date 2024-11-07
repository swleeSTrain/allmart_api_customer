//package org.sunbong.allmart_api.order.service;
//
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import org.sunbong.allmart_api.order.domain.OrderEntity;
//import org.sunbong.allmart_api.order.domain.OrderItem;
//import org.sunbong.allmart_api.order.domain.OrderStatus;
//import org.sunbong.allmart_api.order.dto.OrderCheckDTO;
//import org.sunbong.allmart_api.order.repository.OrderRepository;
//import org.sunbong.allmart_api.order.repository.OrderItemRepository;
//import org.sunbong.allmart_api.order.service.OrderService;
//import org.sunbong.allmart_api.customer.domain.Customer;
//import org.sunbong.allmart_api.product.domain.Product;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//@Transactional
//@Log4j2
//public class OrderServiceIntegrationTest {
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//
//    private OrderEntity orderEntity;
//
//    @BeforeEach
//    public void setUp() {
//        // 1. 더미 Customer 생성
//        Customer customer = new Customer(1L, "010-1234-5678", "John Doe", 0);
//        log.info("Creating dummy Customer: {}", customer);
//
//        // 2. 더미 Product 생성
//        Product product1 = new Product(1L, "Product A", "yb-2", BigDecimal.valueOf(500.0), null);
//        Product product2 = new Product(2L, "Product B", "ra-3", BigDecimal.valueOf(500.0), null);
//        log.info("Creating dummy Products: {}, {}", product1, product2);
//
//        // 3. 더미 OrderEntity 생성 및 저장
//        orderEntity = OrderEntity.builder()
//                .customer(customer)
//                .totalAmount(BigDecimal.valueOf(1000.0))
//                .status(OrderStatus.PENDING)
//                .orderDate(LocalDateTime.now().minusDays(1))
//                .build();
//        orderRepository.save(orderEntity);
//        log.info("Saving OrderEntity: {}", orderEntity);
//
//        // 4. 더미 OrderItem 생성 및 저장
//        OrderItem orderItem1 = OrderItem.builder()
//                .quantity((short) 2)
//                .unitPrice(product1.getPrice())
//                .order(orderEntity)
//                .product(product1)
//                .build();
//
//        OrderItem orderItem2 = OrderItem.builder()
//                .quantity((short) 3)
//                .unitPrice(product2.getPrice())
//                .order(orderEntity)
//                .product(product2)
//                .build();
//
//        orderItemRepository.saveAll(Arrays.asList(orderItem1, orderItem2));
//        log.info("Saving OrderItems: {}, {}", orderItem1, orderItem2);
//    }
//
//    @Test
//    @DisplayName("주문 ID로 주문 조회 테스트 - OrderCheckDTO 반환")
//    public void testFindById() {
//        // Given: 데이터베이스에 미리 저장된 주문 ID 사용
//        Long orderId = orderEntity.getOrderID();
//        log.info("Testing findById with Order ID: {}", orderId);
//
//        // When: 서비스 메서드 호출
//        OrderCheckDTO orderCheckDTO = orderService.findById(orderId);
//        log.info("Retrieved OrderCheckDTO: {}", orderCheckDTO);
//
//        // Then: 결과 검증
//        assertNotNull(orderCheckDTO, "OrderCheckDTO는 null이 아니어야 합니다.");
//        assertEquals(orderId, orderCheckDTO.getOrderID(), "Order ID가 일치해야 합니다.");
//        assertEquals(orderEntity.getCustomer().getName(), orderCheckDTO.getCustomerName(), "고객 이름이 일치해야 합니다.");
//        assertEquals(orderEntity.getCustomer().getPhoneNumber(), orderCheckDTO.getPhoneNumber(), "고객 전화번호가 일치해야 합니다.");
//        assertEquals(2, orderCheckDTO.getOrderItems().size(), "주문 항목 수가 일치해야 합니다.");
//
//        // OrderCheckDTO의 항목 정보 확인
//        orderCheckDTO.getOrderItems().forEach(item ->
//                log.info("OrderItem in OrderCheckDTO - Product ID: {}, Quantity: {}, Unit Price: {}",
//                        item.getProductId(), item.getQuantity(), item.getUnitPrice())
//        );
//    }
//}
