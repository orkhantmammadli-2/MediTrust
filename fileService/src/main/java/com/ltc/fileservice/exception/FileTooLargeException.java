package com.ltc.fileservice.exception;

public class FileTooLargeException extends RuntimeException {
    public FileTooLargeException(String message) {
        super(message);
    }
}
