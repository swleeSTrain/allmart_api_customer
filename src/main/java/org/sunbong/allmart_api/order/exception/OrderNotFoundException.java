package org.sunbong.allmart_api.order.exception;

public class OrderNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Order not found with the specified ID.";

    public OrderNotFoundException(Long orderId) {
        super(DEFAULT_MESSAGE + " Order ID: " + orderId);
    }

}
