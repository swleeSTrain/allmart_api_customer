//package org.sunbong.allmart_api.order.service;
//
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.sunbong.allmart_api.common.dto.PageRequestDTO;
//import org.sunbong.allmart_api.common.dto.PageResponseDTO;
//import org.sunbong.allmart_api.customer.domain.Customer;
//import org.sunbong.allmart_api.order.domain.OrderStatus;
//import org.sunbong.allmart_api.order.dto.OrderDTO;
//import org.sunbong.allmart_api.order.dto.OrderItemDTO;
//import org.sunbong.allmart_api.order.repository.OrderRepository;
//import org.sunbong.allmart_api.order.domain.OrderEntity;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//import java.util.Comparator;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//
//@Log4j2
//public class OrderServiceTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @DisplayName("주문 상태별 필터링 테스트 - PENDING 상태만 조회")
//    void testSearchOrders_PendingStatus() throws Exception {
//
//        // Given
//        String status = "PENDING";
//        PageRequestDTO pageRequestDTO = new PageRequestDTO();
//
//        List<OrderDTO> pendingOrders = Arrays.asList(
//                new OrderDTO(1L, "John Doe", BigDecimal.valueOf(1000.0), "PENDING", LocalDateTime.now().minusDays(5)),
//                new OrderDTO(2L, "Jane Doe", BigDecimal.valueOf(2000.0), "PENDING", LocalDateTime.now().minusDays(3))
//        );
//
//        PageResponseDTO<OrderDTO> mockResponse = PageResponseDTO.<OrderDTO>withAll()
//                .dtoList(pendingOrders)
//                .totalCount(20)
//                .pageRequestDTO(pageRequestDTO)
//                .build();
//
//        given(orderRepository.searchOrders(status, null, pageRequestDTO)).willReturn(mockResponse);
//
//        // 모킹 데이터 확인
//        log.info("Testing searchOrders with status: {}", status);
//        pendingOrders.forEach(order ->
//                log.info("Mock Order Data - Order ID: {}, Customer: {}, Status: {}, Amount: {}",
//                        order.getOrderID(), order.getCustomerName(), order.getStatus(), order.getTotalAmount())
//        );
//
//        // When
//        PageResponseDTO<OrderDTO> result = orderService.searchOrders(status, null, pageRequestDTO);
//
//        // 검색 결과 확인
//        log.info("Search result - Page Request: Page {}, Size {}", pageRequestDTO.getPage(), pageRequestDTO.getSize());
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.getDtoList()).hasSize(2);
//
//        // 반환된 데이터
//        result.getDtoList().forEach(order -> {
//            assertThat(order.getStatus()).isEqualTo("PENDING");
//            log.info("Filtered Result - Order ID: {}, Customer: {}, Status: {}, Amount: {}",
//                    order.getOrderID(), order.getCustomerName(), order.getStatus(), order.getTotalAmount());
//        });
//    }
//
//
//    @Test
//    @DisplayName("고객 정보 필터링 테스트 - 고객 이름으로 검색")
//    void testSearchOrders_ByCustomerName() throws Exception {
//        // Given
//        String customerInfo = "John";
//        PageRequestDTO pageRequestDTO = new PageRequestDTO();
//
//        List<OrderDTO> johnOrders = Arrays.asList(
//                new OrderDTO(1L, "John Doe", BigDecimal.valueOf(1000.0), "PENDING", LocalDateTime.now().minusDays(5)),
//                new OrderDTO(2L, "John Smith", BigDecimal.valueOf(2000.0), "COMPLETED", LocalDateTime.now().minusDays(3))
//        );
//
//        PageResponseDTO<OrderDTO> mockResponse = PageResponseDTO.<OrderDTO>withAll()
//                .dtoList(johnOrders)
//                .totalCount(41)
//                .pageRequestDTO(pageRequestDTO)
//                .build();
//
//        given(orderRepository.searchOrders(null, customerInfo, pageRequestDTO)).willReturn(mockResponse);
//
//        // 모킹된 데이터 확인
//        log.info("Testing searchOrders with customerInfo: {}", customerInfo);
//        johnOrders.forEach(order ->
//                log.info("Mock Order Data - Order ID: {}, Customer: {}, Status: {}",
//                        order.getOrderID(), order.getCustomerName(), order.getStatus())
//        );
//
//        // When
//        PageResponseDTO<OrderDTO> result = orderService.searchOrders(null, customerInfo, pageRequestDTO);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.getDtoList()).hasSize(2);
//
//        // 반환된 데이터에서 고객 이름 확인
//        result.getDtoList().forEach(order -> {
//            assertThat(order.getCustomerName()).containsIgnoringCase(customerInfo);
//            log.info("Filtered Result - Order ID: {}, Customer: {}, Status: {}",
//                    order.getOrderID(), order.getCustomerName(), order.getStatus());
//        });
//
//    }
//
//
//    @Test
//    @DisplayName("복합 조건 검색 테스트 - 상태와 고객 정보 함께 사용")
//    void testSearchOrders_WithStatusAndCustomerInfo() throws Exception {
//
//        // Given
//        String status = "COMPLETED";
//        String customerInfo = "Doe";
//        PageRequestDTO pageRequestDTO = new PageRequestDTO();
//
//        List<OrderDTO> completedDoeOrders = Arrays.asList(
//                new OrderDTO(3L, "John Doe", BigDecimal.valueOf(3000.0), "COMPLETED", LocalDateTime.now().minusDays(2)),
//                new OrderDTO(4L, "Jane Doe", BigDecimal.valueOf(4000.0), "COMPLETED", LocalDateTime.now().minusDays(1))
//        );
//
//        PageResponseDTO<OrderDTO> mockResponse = PageResponseDTO.<OrderDTO>withAll()
//                .dtoList(completedDoeOrders)
//                .totalCount(41)
//                .pageRequestDTO(pageRequestDTO)
//                .build();
//
//        given(orderRepository.searchOrders(status, customerInfo, pageRequestDTO)).willReturn(mockResponse);
//
//
//        log.info("Testing searchOrders with status: {}, customerInfo: {}", status, customerInfo);
//        completedDoeOrders.forEach(order ->
//                log.info("Mock Order Data - Order ID: {}, Customer: {}, Status: {}",
//                        order.getOrderID(), order.getCustomerName(), order.getStatus())
//        );
//
//        // When
//        PageResponseDTO<OrderDTO> result = orderService.searchOrders(status, customerInfo, pageRequestDTO);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.getDtoList()).hasSize(2);
//
//        // 반환된 데이터의 각 주문 정보 확인
//        result.getDtoList().forEach(order -> {
//            assertThat(order.getStatus()).isEqualTo("COMPLETED");
//            assertThat(order.getCustomerName()).containsIgnoringCase(customerInfo);
//
//
//            log.info("Filtered Result - Order ID: {}, Customer: {}, Status: {}",
//                    order.getOrderID(), order.getCustomerName(), order.getStatus());
//        });
//
//
//    }
//
//
//    @Test
//    @DisplayName("페이징 처리 테스트 - 첫 번째 페이지, 10개의 주문")
//    void testSearchOrdersWithPaging() throws Exception {
//        // given
//        String status = "PENDING";
//        PageRequestDTO pageRequestDTO = new PageRequestDTO();
//        Pageable pageable = PageRequest.of(
//                pageRequestDTO.getPage() - 1,
//                pageRequestDTO.getSize()
//        );
//
//        // 10개의 OrderDTO 객체 생성 (주문 날짜가 오래된 순으로 생성됨)
//        List<OrderDTO> pendingOrders = IntStream.rangeClosed(1, 10)
//                .mapToObj(i -> new OrderDTO(
//                        (long) i,
//                        "Customer " + i,
//                        BigDecimal.valueOf(1000 + i * 100),
//                        status,
//                        LocalDateTime.now().minusDays(i))
//                )
//                .sorted(Comparator.comparing(OrderDTO::getOrderDate))
//                .collect(Collectors.toList());
//
//        PageResponseDTO<OrderDTO> mockResponse = PageResponseDTO.<OrderDTO>withAll()
//                .dtoList(pendingOrders)
//                .totalCount(20)
//                .pageRequestDTO(pageRequestDTO)
//                .build();
//
//        given(orderRepository.searchOrders(status, null, pageRequestDTO)).willReturn(mockResponse);
//
//        // when
//        PageResponseDTO<OrderDTO> result = orderService.searchOrders(status, null, pageRequestDTO);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getDtoList()).hasSize(10);
//        assertThat(result.getTotalCount()).isEqualTo(20);
//
//        // 반환된 데이터가 오래된 순서로 정렬되었는지 확인
//        List<OrderDTO> sortedByDate = result.getDtoList().stream()
//                .sorted(Comparator.comparing(OrderDTO::getOrderDate))
//                .collect(Collectors.toList());
//
//        assertThat(result.getDtoList()).isEqualTo(sortedByDate);
//
//        // 페이징된 데이터가 올바른지 확인
//        result.getDtoList().forEach(order -> {
//            assertThat(order.getStatus()).isEqualTo("PENDING");
//            log.info("Order ID: {}, Order Date: {}, Customer: {}",
//                    order.getOrderID(), order.getOrderDate(), order.getCustomerName());
//        });
//
//        log.info("Total Results: {}", result.getTotalCount());
//    }
//
//    @Test
//    @DisplayName("페이징 처리 테스트 - 특정 상태 주문 상태 개수 반환")
//    void testCountOrderByStatusWithPaging() throws Exception {
//
//        // Given
//        String status = "COMPLETED";
//        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
//
//        Pageable pageable = PageRequest.of(
//                pageRequestDTO.getPage() - 1,
//                pageRequestDTO.getSize()
//        );
//
//        // 12개의 OrderDTO 객체 생성 (완료된 주문이 최신 순으로 정렬)
//        List<OrderDTO> completedOrders = IntStream.rangeClosed(1, 12)
//                .mapToObj(i -> new OrderDTO(
//                        (long) i,
//                        "Customer " + i,
//                        BigDecimal.valueOf(1000 + i * 100),
//                        status,
//                        LocalDateTime.now().minusMinutes(i))
//                )
//                .sorted(Comparator.comparing(OrderDTO::getOrderDate).reversed())
//                .collect(Collectors.toList());
//
//        // 전체 주문에서 페이징된 데이터만 설정
//        List<OrderDTO> pagedOrders = completedOrders.stream()
//                .limit(pageRequestDTO.getSize()) // 최대 10개의 결과만 가져오기
//                .collect(Collectors.toList());
//
//        PageResponseDTO<OrderDTO> mockResponse = PageResponseDTO.<OrderDTO>withAll()
//                .dtoList(pagedOrders)
//                .totalCount(completedOrders.size())
//                .pageRequestDTO(pageRequestDTO)
//                .build();
//
//        given(orderRepository.searchOrders(status, null, pageRequestDTO)).willReturn(mockResponse);
//
//        // When
//        PageResponseDTO<OrderDTO> result = orderService.searchOrders(status, null, pageRequestDTO);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.getDtoList()).hasSize(10); // 페이징된 결과 개수는 10이어야 함
//        assertThat(result.getTotalCount()).isEqualTo(12); // 전체 개수는 12개
//
//        // 페이징된 데이터가 최신 완료 순으로 정렬되었는지 확인
//        List<OrderDTO> sortedByDateDesc = result.getDtoList().stream()
//                .sorted(Comparator.comparing(OrderDTO::getOrderDate).reversed())
//                .collect(Collectors.toList());
//
//        assertThat(result.getDtoList()).isEqualTo(sortedByDateDesc);
//
//        // 로그로 결과 확인
//        result.getDtoList().forEach(order ->
//                log.info("Paged Order - Order ID: {}, Customer: {}, Status: {}, Order Date: {}",
//                        order.getOrderID(), order.getCustomerName(), order.getStatus(), order.getOrderDate())
//        );
//    }
//
//
//
//}