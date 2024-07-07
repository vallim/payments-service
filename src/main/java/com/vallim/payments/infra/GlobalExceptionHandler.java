package com.vallim.payments.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<?> handle(RuntimeException ex) {
        logger.error("Illegal Argument: ", ex);

        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleError(Exception ex) {
        logger.error("Unexpected Server Error", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The system is unavailable");
    }
}
