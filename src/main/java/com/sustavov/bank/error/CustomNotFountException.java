package com.sustavov.bank.error;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
public class CustomNotFountException extends RuntimeException {

    public CustomNotFountException() {
        super();
    }

    public CustomNotFountException(String message) {
        super(message);
    }

    public CustomNotFountException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomNotFountException(Throwable cause) {
        super(cause);
    }
}
