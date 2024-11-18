package com.food.ordering.system.kafka.producer.service;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;
import java.io.Serializable;

@Slf4j
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {
    private final KafkaTemplate<K, V> kafkaTemplate;
    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public void send(String topic, K key, V message, ListenableFutureCallback<SendResult<K, V>> callback) {
        log.info("Sending message to topic {}", topic);
        try {

            ListenableFuture<SendResult<K, V>> kafkaResult = (ListenableFuture<SendResult<K, V>>) kafkaTemplate.send(topic, key, message);
            kafkaResult.addCallback(callback);
        } catch (KafkaException e) {
            log.error("Error while sending message to topic {}", topic, e);
            throw new KafkaProducerException("Error while sending message " + message +  " to topic " + topic  + ": " + e.getMessage());
        }
    }
    @PreDestroy
    public void close() {
        if(kafkaTemplate != null) {
            log.info("Closing kafka producer");
            kafkaTemplate.destroy();
        }
    }
}
