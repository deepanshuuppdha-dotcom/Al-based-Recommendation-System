package com.recsys.exception;

/** Thrown when login fails or a user is not allowed to proceed. */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) { super(message); }
}
