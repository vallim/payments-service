package com.vallim.payments.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AesCardNumberCryptoTest {

    private AesCardNumberCrypto aesCardNumberCrypto;

    @BeforeEach
    public void setUp() {
        aesCardNumberCrypto = new AesCardNumberCrypto("cGF5bWVudHNzZXJ2aWNlaw==");
    }

    @Test
    public void testEncrypt_encryptsDataUsingAES() throws Exception {
        String data = "1234567890123456";
        String encryptedData = aesCardNumberCrypto.encrypt(data);

        assertAll("Encrypted data assertions",
                () -> assertNotNull(encryptedData, "Encrypted data should not be null"));
    }

    @Test
    public void testDecrypt_decryptsEncryptedData() throws Exception {
        String data = "1234567890123456";
        String encryptedData = aesCardNumberCrypto.encrypt(data);

        String decryptedData = aesCardNumberCrypto.decrypt(encryptedData);

        assertEquals(data, decryptedData);
    }
}
