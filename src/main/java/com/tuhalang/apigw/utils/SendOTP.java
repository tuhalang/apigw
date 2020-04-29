package com.tuhalang.apigw.utils;

import com.tuhalang.apigw.bean.EmailTransfer;
import com.tuhalang.apigw.service.KafkaService;

import java.util.Random;

public class SendOTP {

    private static final int MIN_OTP = 100000;
    private static final int MAX_OTP = 999999;

    private static String generateOTP(){
        Random random = new Random();
        int otp = random.nextInt(MAX_OTP-MIN_OTP+1);
        return String.valueOf(otp);
    }

    public static String sendOTPViaEmail(String email, KafkaService kafkaService){
        String otp = generateOTP();
        String subject = "OTP CODE";
        String content = "YOUR OTP CODE IS " + otp;

        EmailTransfer emailTransfer = new EmailTransfer();
        emailTransfer.setSubject(subject);
        emailTransfer.setContent(content);
        emailTransfer.setEmail(email);

        kafkaService.transferMail(emailTransfer);
        return otp;
    }
}
