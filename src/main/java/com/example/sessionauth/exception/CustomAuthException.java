package com.example.sessionauth.exception;

import javax.naming.AuthenticationException;

public class CustomAuthException extends AuthenticationException {
    public CustomAuthException(String msg) {
        super(msg);
    }
}
