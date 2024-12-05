package org.sunbong.allmart_api.customer.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)  // 경고 해결
public class CustomerTaskException extends RuntimeException {

    private final int status;
    private final String msg;

    public CustomerTaskException(final int status, final String msg) {
        super(status + "_" + msg);
        this.status = status;
        this.msg = msg;
    }
}
