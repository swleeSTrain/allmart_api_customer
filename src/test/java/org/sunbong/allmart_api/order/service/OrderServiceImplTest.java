package org.sunbong.allmart_api.order.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.mock.FakeOrderSearch;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.domain.OrderStatus;
import org.sunbong.allmart_api.order.dto.OrderListDTO;
import org.sunbong.allmart_api.order.exception.OrderNotFoundException;
import org.sunbong.allmart_api.order.repository.OrderJpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    private OrderServiceImpl orderService;
    private OrderJpaRepository orderRepository;  // 실제 JPA 리포지토리를 그대로 사용
    private FakeOrderSearch fakeOrderSearch;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderJpaRepository.class);  // 기본 JPA 기능만 사용하도록 mock 처리
        fakeOrderSearch = new FakeOrderSearch(); // 가짜 검색 기능
        orderService = new OrderServiceImpl(orderRepository, fakeOrderSearch);
    }

    @Test
    @DisplayName("getOrderById는_존재하는_주문을_반환한다")
    void getOrderByIdTest() {
        // given
        Long orderId = 1L;
        OrderEntity mockOrder = OrderEntity.builder()
                .orderID(orderId)
                .customerId("customer1")
                .totalAmount(BigDecimal.valueOf(100.00))
                .status(OrderStatus.PENDING)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // when
        OrderListDTO result = orderService.getOrderById(orderId);

        // then
        assertThat(result.getOrderId()).isEqualTo(orderId);
        assertThat(result.getCustomerId()).isEqualTo("customer1");
        assertThat(result.getTotalAmount()).isEqualTo(BigDecimal.valueOf(100.00));
    }

    @Test
    @DisplayName("getOrderById는_존재하지_않는_주문일_경우_예외를_던진다")
    void getOrderByIdFailTest() {
        // given
        Long nonExistentOrderId = 999L;

        // when & then
        assertThatThrownBy(() -> orderService.getOrderById(nonExistentOrderId))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("searchOrders는_가짜_데이터를_반환한다")
    void searchOrdersTest() {
        // given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(5)
                .build();

        // when
        PageResponseDTO<OrderListDTO> response = orderService.searchOrders(null, null, pageRequestDTO);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getDtoList().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("changeOrderStatus는_주문_상태를_변경한다")
    void changeOrderStatusTest() {
        // given
        Long orderId = 1L;
        OrderEntity mockOrder = OrderEntity.builder()
                .orderID(orderId)
                .customerId("customer1")
                .totalAmount(BigDecimal.valueOf(100.00))
                .status(OrderStatus.PENDING)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // when
        orderService.changeOrderStatus(orderId, OrderStatus.COMPLETED);

        // then
        assertThat(mockOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }
}
