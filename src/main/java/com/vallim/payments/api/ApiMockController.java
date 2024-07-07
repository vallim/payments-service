package com.vallim.payments.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api-mock")
@RestController
public class ApiMockController {

    private static final Logger logger = LoggerFactory.getLogger(ApiMockController.class);

    @PostMapping(value = "/success")
    public ResponseEntity success(@RequestBody String body) {
        logger.info("[SUCCESS] mock called for payload {}", body);
        return ResponseEntity.status(200).build();
    }

    @PostMapping(value = "/fail")
    public ResponseEntity fail(@RequestBody String body) {
        logger.error("[FAIL] mock called for payload {}", body);
        return ResponseEntity.status(400).build();
    }
}
