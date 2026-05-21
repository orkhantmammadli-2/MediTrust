package com.ltc.fileservice.exception;


import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<String> handleInvalidFileType(InvalidFileTypeException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());}
    @ExceptionHandler( FileSizeLimitExceededException.class)
    public ResponseEntity<String> handleFileSize(FileSizeLimitExceededException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<String> handleEmptyFile(EmptyFileException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAwsError(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File upload failed: "+ ex.getMessage());
    }
}
