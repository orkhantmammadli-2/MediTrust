package com.ltc.fileservice.exception;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(String message) {
        super(message);
    }
}
