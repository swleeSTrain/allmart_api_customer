package org.sunbong.allmart_api.order.exception;

public class OrderStatusChangeException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Order status change is not allowed due to the current order state.";
    private static final String COMPLETED_STATUS_MESSAGE = "Cannot change the status of a completed order.";
    private static final String CANCELLED_STATUS_MESSAGE = "Cannot change the status of a cancelled order.";

    // 기본 메시지
    public OrderStatusChangeException() {
        super(DEFAULT_MESSAGE);
    }

    // 주문이 완료되었는지 여부에 따른 메시지 설정
    public OrderStatusChangeException(boolean isCompleted) {
        super(isCompleted ? COMPLETED_STATUS_MESSAGE : CANCELLED_STATUS_MESSAGE);
    }
}
