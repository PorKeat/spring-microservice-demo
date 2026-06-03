package dev.sample.productservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidCategoryException extends RuntimeException {

    private final HttpStatus status;

    public InvalidCategoryException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public InvalidCategoryException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
