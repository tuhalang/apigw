package com.tuhalang.apigw.utils;

public enum JedisDB {

    JEDIS_DB_REGISTER(0),
    JEDIS_DB_OTP_REGISTER(1),
    JEDIS_DB_LOGIN(2),
    JEDIS_DB_SESSION(4),
    JEDIS_DB_BLACK_LIST(5),
    JEDIS_DB_OTP(6);


    private int key;
    JedisDB(int key) {
        this.key = key;
    }

    public int getKey(){
        return this.key;
    }
}
