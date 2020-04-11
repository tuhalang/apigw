package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgApiDao;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgApiDaoImpl implements AgApiDao {
}
