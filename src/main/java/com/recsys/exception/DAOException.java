package com.recsys.exception;

/**
 * Runtime exception that wraps any error raised in the DAO (database) layer.
 */
public class DAOException extends RuntimeException {
    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
