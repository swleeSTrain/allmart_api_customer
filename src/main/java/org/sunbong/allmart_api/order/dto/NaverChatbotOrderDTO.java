package org.sunbong.allmart_api.order.dto;

import lombok.Data;
import org.sunbong.allmart_api.product.domain.Product;

import java.util.ArrayList;
import java.util.List;

@Data
public class NaverChatbotOrderDTO {
    private String userId;
    private String productName;
    private int quantity;
}
