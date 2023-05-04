package com.d2y.d2yspringapi.exceptions;

public class D2YException extends RuntimeException {
    public D2YException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public D2YException(String exMessage) {
        super(exMessage);
    }

}
