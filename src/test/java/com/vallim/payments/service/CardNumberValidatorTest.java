package com.vallim.payments.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardNumberValidatorTest {

    private CardNumberValidator cardNumberValidator = new CardNumberValidator();

    @Test
    public void shouldReturnTrueForValidCardNumber() {
        boolean isValid = cardNumberValidator.isValid("4111111111111111");

        assertTrue(isValid);
    }

    @Test
    public void shouldReturnFalseForInvalidCardNumber() {
        boolean isValid = cardNumberValidator.isValid("4111111111111");

        assertFalse(isValid);
    }
}
