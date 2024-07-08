package com.vallim.payments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AesCardNumberCrypto {

    private SecretKey secretKey;

    @Autowired
    public AesCardNumberCrypto(@Value("${card_number.secret.key}") String secretKey) {
        this.secretKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "AES");
    }

    public AesCardNumberCrypto() {}

    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception ex) {
            throw new RuntimeException("Error while encrypting card number", ex);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedBytes);
            return new String(decryptedData);
        } catch (Exception ex) {
            throw new RuntimeException("Error while decrypting card number", ex);
        }
    }
}
