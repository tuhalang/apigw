package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.bean.EmailTransfer;
import com.tuhalang.apigw.domain.AgLog;
import com.tuhalang.apigw.service.KafkaService;
import com.tuhalang.apigw.utils.ConfigApp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@PropertySource({"classpath:kafka.properties"})
public class KafkaServiceImpl implements KafkaService {

    private static final Logger LOGGER = Logger.getLogger(KafkaServiceImpl.class);

    @Autowired
    @Qualifier("log_transaction")
    private KafkaTemplate loggingKafkaTemplate;

    @Autowired
    @Qualifier("mail_transfer")
    private KafkaTemplate mailKafkaTemplate;

    @Autowired
    private Environment environment;

    @Override
    public void writeLogTransaction(Object object) {
        if(object instanceof AgLog){
            AgLog agLog = (AgLog) object;
            String topic = environment.getRequiredProperty(ConfigApp.LOGGING_TRANSACTION_TOPIC);
            sendMsg(topic, agLog, loggingKafkaTemplate);
        }
    }

    @Override
    public void writeLogAccess(Object object) {

    }

    @Override
    public void transferMail(Object object) {
        if(object instanceof EmailTransfer){
            EmailTransfer emailTransfer = (EmailTransfer) object;
            String topic = environment.getProperty(ConfigApp.EMAIL_TRANSFER_TOPIC);
            sendMsg(topic, emailTransfer, mailKafkaTemplate);
        }
    }

    public <T> void sendMsg(String topic, T t, KafkaTemplate kafkaTemplate) {

        ListenableFuture<SendResult<String, T>> future =
                kafkaTemplate.send(topic, t);

        future.addCallback(new ListenableFutureCallback<SendResult<String, T>>() {

            @Override
            public void onSuccess(SendResult<String, T> result) {
                LOGGER.info("Sent message=[" + t +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.error("Unable to send message=["
                        + t + "] due to : " + ex.getMessage());
            }
        });
    }
}
