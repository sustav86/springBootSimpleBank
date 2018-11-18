package com.sustavov.bank.error;

import lombok.Data;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
@Data
public class CustomErrorResponse {

    private int status;
    private String message;
    private long timeStamp;

    public CustomErrorResponse() {
    }

    public CustomErrorResponse(int status, String message, long timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }
}
