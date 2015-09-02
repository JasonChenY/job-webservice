package com.tiaonaer.ws.job.exception;

/**
 * Created by echyong on 9/1/15.
 */
public class CustomRequestException extends Exception {
    private String message;
    public CustomRequestException(String message) {
        super(message);
        this.message = message;
    }
    public String getMessage() { return message; }
}
