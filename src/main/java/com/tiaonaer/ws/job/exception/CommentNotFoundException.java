package com.tiaonaer.ws.job.exception;

/**
 * Created by echyong on 8/27/15.
 */
public class CommentNotFoundException extends Exception {
    private String message;
    public CommentNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() { return message; }
}
