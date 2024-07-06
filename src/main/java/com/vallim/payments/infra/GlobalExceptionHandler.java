package com.vallim.payments.infra;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<?> handle(RuntimeException ex) {
        //log.error("Bad Request", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleError(Exception ex) {
        //log.error("Internal error server", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The system is unavailable");
    }
}
