package org.sunbong.allmart_api.order.repository.search;

import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.order.domain.OrderStatus;
import org.sunbong.allmart_api.order.dto.OrderListDTO;

public interface OrderSearch {
    PageResponseDTO<OrderListDTO> searchOrders(OrderStatus status, String customerId, PageRequestDTO pageRequestDTO);
}
