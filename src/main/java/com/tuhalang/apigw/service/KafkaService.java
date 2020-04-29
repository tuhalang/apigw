package com.tuhalang.apigw.service;

public interface KafkaService {
    void writeLogTransaction(Object object);
    void writeLogAccess(Object object);
    void transferMail(Object object);
}
