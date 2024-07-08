package com.vallim.payments.api;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = "/api-mock")
@RestController
public class ApiMockController {

    private static final Logger logger = LoggerFactory.getLogger(ApiMockController.class);

    @Operation(
            summary = "Mock a successful response",
            description = "Mocks a response with HTTP status 200 (OK). This endpoint logs the provided payload and returns a status 200 response. Useful for testing purposes."
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/success")
    public ResponseEntity success(@RequestBody String body) {
        logger.info("[SUCCESS] mock called for payload {}", body);
        return ResponseEntity.status(200).build();
    }

    @Operation(
            summary = "Mock a failure response",
            description = "Mocks a response with HTTP status 400 (Bad Request). This endpoint logs the provided payload and returns a status 400 response. Useful for testing failure scenarios."
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @PostMapping(value = "/fail")
    public ResponseEntity fail(@RequestBody String body) {
        logger.error("[FAIL] mock called for payload {}", body);
        return ResponseEntity.status(400).build();
    }
}
