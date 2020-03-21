package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.Account;

public interface AccountDao {
    void save(Account account) throws Exception;
    Account findByUsername(String username) throws Exception;
}
