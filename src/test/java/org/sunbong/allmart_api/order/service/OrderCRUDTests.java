//package org.sunbong.allmart_api.order.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.BDDMockito.given;
//
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.sunbong.allmart_api.customer.domain.Customer;
//import org.sunbong.allmart_api.order.domain.OrderEntity;
//import org.sunbong.allmart_api.order.domain.OrderItem;
//import org.sunbong.allmart_api.order.domain.OrderStatus;
//import org.sunbong.allmart_api.order.dto.OrderCheckDTO;
//import org.sunbong.allmart_api.order.dto.OrderItemDTO;
//import org.sunbong.allmart_api.order.repository.OrderItemRepository;
//import org.sunbong.allmart_api.order.repository.OrderRepository;
//import org.sunbong.allmart_api.order.service.OrderService;
//import org.sunbong.allmart_api.product.domain.Product;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Log4j2
//@ExtendWith(MockitoExtension.class)
//public class OrderCRUDTests {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private OrderItemRepository orderItemRepository;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    private OrderEntity mockOrderEntity;
//    private List<OrderItemDTO> mockOrderItems;
//    private Customer mockCustomer;
//
//    @BeforeEach
//    public void setUp() {
//        // Mock Customer 객체 생성
//        mockCustomer = new Customer(1L, "010-1234-5678", "John Doe", 0);
//
//        // Mock OrderEntity 객체 생성
//        mockOrderEntity = OrderEntity.builder()
//                .orderID(1L)
//                .customer(mockCustomer)
//                .totalAmount(BigDecimal.valueOf(1000.0))
//                .status(OrderStatus.PENDING)
//                .orderDate(LocalDateTime.now().minusDays(1))
//                .build();
//
//        // Mock Product 객체 생성
//        Product product1 = new Product(1L, "Product A", "yb-2", BigDecimal.valueOf(500.0), null);
//        Product product2 = new Product(2L, "Product B", "ra-3", BigDecimal.valueOf(500.0), null);
//
//        // Mock OrderItemDTO 객체 리스트 생성
//        mockOrderItems = Arrays.asList(
//                new OrderItemDTO(product1.getProductID(), product1.getName(), (short) 2, product1.getPrice(), product1.getAttachFiles()),
//                new OrderItemDTO(product2.getProductID(), product2.getName(), (short) 3, product2.getPrice(), product2.getAttachFiles())
//        );
//    }
//
//    @Test
//    @DisplayName("주문 ID로 주문 조회 테스트 - OrderCheckDTO 반환")
//    public void testFindById() {
//
//        // Given
//
//        given(orderRepository.findById(1L)).willReturn(Optional.of(mockOrderEntity));
//
//        given(orderItemRepository.findByOrder(mockOrderEntity)).willReturn(
//                mockOrderItems.stream()
//                        .map(item -> new OrderItem(
//                                item.getProductId(),
//                                item.getQuantity(),
//                                item.getUnitPrice(),
//                                mockOrderEntity,
//                                new Product(item.getProductId(), item.getProductName(), null, item.getUnitPrice(), null)
//                        ))
//                        .collect(Collectors.toList())
//        );
//
//        // When
//
//        OrderCheckDTO orderCheckDTO = orderService.findById(1L);
//
//        // Then
//
//        assertNotNull(orderCheckDTO, "OrderCheckDTO는 null이 아니어야 합니다.");
//
//        log.info("검증 - Order ID: 기대 값 = {}, 실제 값 = {}", mockOrderEntity.getOrderID(), orderCheckDTO.getOrderID());
//        assertEquals(mockOrderEntity.getOrderID(), orderCheckDTO.getOrderID(), "Order ID가 일치해야 합니다.");
//
//        log.info("검증 - Customer Name: 기대 값 = {}, 실제 값 = {}", mockCustomer.getName(), orderCheckDTO.getCustomerName());
//        assertEquals(mockCustomer.getName(), orderCheckDTO.getCustomerName(), "고객 이름이 일치해야 합니다.");
//
//        log.info("검증 - Phone Number: 기대 값 = {}, 실제 값 = {}", mockCustomer.getPhoneNumber(), orderCheckDTO.getPhoneNumber());
//        assertEquals(mockCustomer.getPhoneNumber(), orderCheckDTO.getPhoneNumber(), "고객 전화번호가 일치해야 합니다.");
//
//        log.info("검증 - Order Items 수: 기대 값 = {}, 실제 값 = {}", mockOrderItems.size(), orderCheckDTO.getOrderItems().size());
//        assertEquals(mockOrderItems.size(), orderCheckDTO.getOrderItems().size(), "주문 항목 수가 일치해야 합니다.");
//
//        // OrderCheckDTO 내부의 OrderItemDTO 정보 검증
//        for (int i = 0; i < mockOrderItems.size(); i++) {
//            OrderItemDTO expectedItem = mockOrderItems.get(i);
//            OrderItemDTO actualItem = orderCheckDTO.getOrderItems().get(i);
//
//            log.info("검증 - OrderItemDTO #{}: 기대 값 = {}, 실제 값 = {}", i + 1, expectedItem, actualItem);
//
//            assertEquals(expectedItem.getProductId(), actualItem.getProductId(), "Product ID가 일치해야 합니다.");
//            assertEquals(expectedItem.getProductName(), actualItem.getProductName(), "Product Name이 일치해야 합니다.");
//            assertEquals(expectedItem.getQuantity(), actualItem.getQuantity(), "Quantity가 일치해야 합니다.");
//            assertEquals(expectedItem.getUnitPrice(), actualItem.getUnitPrice(), "Unit Price가 일치해야 합니다.");
//        }
//
//    }
//}
