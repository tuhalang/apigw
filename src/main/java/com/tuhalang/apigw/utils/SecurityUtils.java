package com.tuhalang.apigw.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class SecurityUtils {
    public static String generateSalt(int length){
        return BCrypt.gensalt();
    }

    public static String hashPassword(String plaintext, String salt){
        String ciphertext = BCrypt.hashpw(plaintext, salt);
        return ciphertext;
    }

    public static boolean validPass(String plaintext, String salt, String ciphertext){
        return BCrypt.checkpw(plaintext+salt, ciphertext);
    }
}
