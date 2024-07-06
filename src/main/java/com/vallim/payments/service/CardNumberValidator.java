package com.vallim.payments.service;

import org.springframework.stereotype.Component;

@Component
public class CardNumberValidator {

    public boolean isValid(String cardNumber) {
        int nDigits = cardNumber.length();
        int sum = 0;
        boolean isSecond = false;

        for (int i = nDigits - 1; i >= 0; i--) {
            int d = cardNumber.charAt(i) - '0';

            if (isSecond) {
                d = d * 2;
            }

            // Add two digits to handle cases that make two digits after doubling
            sum += d / 10;
            sum += d % 10;

            isSecond = !isSecond;
        }
        return (sum % 10 == 0);
    }
}
