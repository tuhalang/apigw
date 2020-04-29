package com.tuhalang.apigw.utils;

public class ConfigApp {

    public static final Integer USER_REGISTER_CACHE_TIME = 120;
    public static final Integer OTP_CACHE_TIME = 120;
    public static final Integer BACK_LIST_BLOCK_TIME = 15*60;
    public static final Integer MAX_TIMES_WRONG_PASS = 5;
    public static final Integer MAX_TIMS_SEND_OTP_PER_DAY = 5;
    public static final Integer MIN_DISTANCE_BETWEEN_TWO_TIME_SEND_OTP = 60000;

    //Kafka
    public static final String KAFKA_SERVER = "kafka.server";
    public static final String LOGGING_TRANSACTION_TOPIC = "logging.transaction.topic";
    public static final String EMAIL_TRANSFER_TOPIC = "email.transfer.topic";
    public static final String REDIS_SERVER = "redis.server";
    public static final String MASTER_NAME = "sentinel.master";
}
