package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgErrorCodeDao;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgErrorCodeDaoImpl implements AgErrorCodeDao {
}
