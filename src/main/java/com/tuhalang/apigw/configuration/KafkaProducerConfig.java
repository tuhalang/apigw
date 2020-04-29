package com.tuhalang.apigw.configuration;

import com.tuhalang.apigw.bean.EmailTransfer;
import com.tuhalang.apigw.domain.AgLog;
import com.tuhalang.apigw.utils.ConfigApp;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource({"classpath:kafka.properties"})
public class KafkaProducerConfig {

    @Autowired
    private Environment environment;

    @Bean
    public ProducerFactory<String, String> producerFactory() throws UnknownHostException {
        String server = environment.getRequiredProperty(ConfigApp.KAFKA_SERVER);
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() throws UnknownHostException {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, AgLog> loggingProducerFactory() {
        String server = environment.getRequiredProperty(ConfigApp.KAFKA_SERVER);
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ProducerFactory<String, EmailTransfer> mailProducerFactory() {
        String server = environment.getRequiredProperty(ConfigApp.KAFKA_SERVER);
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean("log_transaction")
    public KafkaTemplate<String, AgLog> loggingKafkaTemplate() {
        return new KafkaTemplate<>(loggingProducerFactory());
    }

    @Bean("mail_transfer")
    public KafkaTemplate<String, EmailTransfer> mailKafkaTemplate() {
        return new KafkaTemplate<>(mailProducerFactory());
    }


}
