package com.ltc.fileservice.exception;

public class EmptyFileException extends RuntimeException {
    public EmptyFileException(String message) {
        super(message);
    }
}
