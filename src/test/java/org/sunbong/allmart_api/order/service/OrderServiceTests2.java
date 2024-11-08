//package org.sunbong.allmart_api.order.service;
//
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import org.sunbong.allmart_api.common.dto.PageRequestDTO;
//import org.sunbong.allmart_api.common.dto.PageResponseDTO;
//import org.sunbong.allmart_api.customer.domain.Customer;
//import org.sunbong.allmart_api.customer.repository.CustomerRepository;
//import org.sunbong.allmart_api.order.domain.OrderEntity;
//import org.sunbong.allmart_api.order.domain.OrderStatus;
//import org.sunbong.allmart_api.order.dto.OrderDetailDTO;
//import org.sunbong.allmart_api.order.dto.OrderListDTO;
//import org.sunbong.allmart_api.order.repository.OrderRepository;
//import org.sunbong.allmart_api.payment.domain.Payment;
//import org.sunbong.allmart_api.payment.domain.PaymentMethod;
//import org.sunbong.allmart_api.payment.repository.PaymentRepository;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//@Log4j2
//public class OrderServiceTests2 {
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    private Long dummyOrderId;
//
//    @BeforeEach
//    public void setup() {
//        log.info("Setting up dummy data for OrderServiceTests2");
//
//        // Create a dummy customer
//        Customer customer = Customer.builder()
//                .name("John Doe")
//                .phoneNumber("01012345678")
//                .loyaltyPoints(100)
//                .build();
//        customerRepository.save(customer);
//        log.info("Saved Customer: {}", customer);
//
//        // Create a dummy payment
//        Payment payment = Payment.builder()
//                .method(PaymentMethod.MEET_WITH_CARD)
//                .amount(new BigDecimal("100.00"))
//                .completed(1)
//                .build();
//        paymentRepository.save(payment);
//        log.info("Saved Payment: {}", payment);
//
//        // Create a dummy order
//        OrderEntity order = OrderEntity.builder()
//                .customer(customer)
//                .status(OrderStatus.PENDING)
//                .totalAmount(new BigDecimal("100.00"))
//                .payment(payment)
//                .build();
//        orderRepository.save(order);
//        log.info("Saved OrderEntity: {}", order);
//
//        // Set the order ID for use in tests
//        dummyOrderId = order.getOrderID();
//    }
//
//    @Test
//    public void testGetOrderDetails() {
//        log.info("Executing testGetOrderDetails with orderId: {}", dummyOrderId);
//
//        // Retrieve the order details using the service
//        OrderDetailDTO orderDetailDTO = orderService.getOrderDetails(dummyOrderId);
//
//        // Log the result from the service with detailed fields
//        log.info("Retrieved OrderDetailDTO: {}", orderDetailDTO);
//        log.info("Order Status: {}", orderDetailDTO.getOrderStatus());
//        log.info("Payment Method: {}", orderDetailDTO.getPaymentMethod());
//        log.info("Customer ID: {}", orderDetailDTO.getCustomerId());
//        log.info("Total Amount: {}", orderDetailDTO.getTotalAmount());
//        log.info("Order Date: {}", orderDetailDTO.getOrderDate());
//
//        // Validate the retrieved details
//        assertNotNull(orderDetailDTO, "OrderDetailDTO should not be null");
//        assertEquals("PENDING", orderDetailDTO.getOrderStatus());
//        assertEquals("MEET_WITH_CARD", orderDetailDTO.getPaymentMethod());
//        assertEquals(dummyOrderId, orderDetailDTO.getCustomerId());
//        assertEquals(new BigDecimal("100.00"), orderDetailDTO.getTotalAmount());
//
//    }
//
//    @BeforeEach
//    public void setupMultipleDummyData() {
//        // 기존 더미 데이터 외 여러 더미 데이터 추가 생성
//        for (int i = 2; i <= 55; i++) {
//            Customer customer = Customer.builder()
//                    .name("Customer " + i)
//                    .phoneNumber("0101234" + String.format("%04d", i))
//                    .build();
//            customerRepository.save(customer);
//
//            Payment payment = Payment.builder()
//                    .method(PaymentMethod.MEET_WITH_CASH)
//                    .amount(new BigDecimal("50.00").add(new BigDecimal(i)))
//                    .completed(1)
//                    .build();
//            paymentRepository.save(payment);
//
//            OrderEntity order = OrderEntity.builder()
//                    .customer(customer)
//                    .status(i % 2 == 0 ? OrderStatus.PENDING : OrderStatus.COMPLETED)
//                    .totalAmount(new BigDecimal("50.00").add(new BigDecimal(i)))
//                    .payment(payment)
//                    .build();
//            orderRepository.save(order);
//        }
//        log.info("50 dummy orders created for paging, search, and sort tests");
//    }
//
//    @Test
//    public void testPaging() {
//        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
//                .page(1)
//                .size(10)
//                .build();
//
//        PageResponseDTO<OrderListDTO> response = orderService.getOrderList(null, null, pageRequestDTO);
//
//        assertEquals(10, response.getDtoList().size(), "Page size should be 10");
//        assertTrue(response.getTotalCount() >= 50, "Total count should be at least 50");
//
//        log.info("Paged Order List: {}", response.getDtoList());
//    }
//
//    @Test
//    public void testSearchByStatus() {
//        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
//                .page(1)
//                .size(10)
//                .build();
//
//        PageResponseDTO<OrderListDTO> response = orderService.getOrderList("PENDING", null, pageRequestDTO);
//
//        assertTrue(response.getDtoList().stream().allMatch(dto -> "PENDING".equals(dto.getStatus())),
//                "All orders should have status PENDING");
//
//        log.info("Filtered by PENDING status: {}", response.getDtoList());
//    }
//
//    @Test
//    public void testSearchByCustomerName() {
//        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
//                .page(1)
//                .size(10)
//                .build();
//
//        PageResponseDTO<OrderListDTO> response = orderService.getOrderList(null, "Customer 1", pageRequestDTO);
//
//        assertTrue(response.getDtoList().stream().allMatch(dto -> dto.getCustomerName().contains("Customer 1")),
//                "All orders should have customer name containing 'Customer 1'");
//
//        log.info("Filtered by customer name 'Customer 1': {}", response.getDtoList());
//    }
//
//    @Test
//    public void testSorting() {
//        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
//                .page(1)
//                .size(10)
//                .build();
//
//        PageResponseDTO<OrderListDTO> response = orderService.getOrderList(null, null, pageRequestDTO);
//
//        List<OrderListDTO> orderList = response.getDtoList();
//
//        // PENDING 상태가 먼저 오도록 정렬되었는지 확인
//        boolean pendingFirst = orderList.stream()
//                .allMatch(dto -> "PENDING".equals(dto.getStatus()) || orderList.get(0).getStatus().equals("PENDING"));
//        assertTrue(pendingFirst, "The list should prioritize PENDING orders.");
//
//        // 이후 createdDate가 오래된 순서대로 정렬되었는지 확인
//        for (int i = 0; i < orderList.size() - 1; i++) {
//            assertTrue(
//                    orderList.get(i).getOrderDate().isBefore(orderList.get(i + 1).getOrderDate()) ||
//                            orderList.get(i).getOrderDate().isEqual(orderList.get(i + 1).getOrderDate()),
//                    "The orders should be sorted by createdDate in ascending order."
//            );
//        }
//
//        // 만약 createdDate가 같은 경우, modifiedDate로 내림차순 확인
//        for (int i = 0; i < orderList.size() - 1; i++) {
//            if (orderList.get(i).getOrderDate().isEqual(orderList.get(i + 1).getOrderDate())) {
//                assertTrue(
//                        orderList.get(i).getOrderDate().isAfter(orderList.get(i + 1).getOrderDate()) ||
//                                orderList.get(i).getOrderDate().isEqual(orderList.get(i + 1).getOrderDate()),
//                        "For same createdDate, orders should be sorted by modifiedDate in descending order."
//                );
//            }
//        }
//
//        log.info("Sorted Order List: {}", response.getDtoList());
//    }
//
//
//    @Test
//    public void testPagingNavigation() {
//        // Page size를 5로 설정하고 총 50개의 데이터가 있을 때 10 페이지까지 가는지 확인
//        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
//                .page(11)
//                .size(5)
//                .build();
//
//        PageResponseDTO<OrderListDTO> response = orderService.getOrderList(null, null, pageRequestDTO);
//
//        // 11페이지 요청 시 이전 페이지가 존재하는지 확인
//        assertTrue(response.isPrev(), "Previous page should be available on page 11");
//
//        // 총 50개 데이터 중 5개씩 나누면 10 페이지가 마지막 페이지이므로 다음 페이지는 없어야 함
//        assertFalse(response.isNext(), "Next page should not be available beyond last page");
//
//        log.info("Pagination navigation on page 11: isPrev={} isNext={}", response.isPrev(), response.isNext());
//    }
//
//
//
//}
