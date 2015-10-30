package com.tiaonr.ws.user.exception;

/**
 * Created by echyong on 10/11/15.
 */
public class UserRegisterException extends Exception {
    private String message;
    public UserRegisterException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() { return message; }
}

