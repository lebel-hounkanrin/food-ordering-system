package com.food.ordering.system.order.service.messaging.publisher.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class OrderKafkaMessageHelper {

    public <T> ListenableFutureCallback<SendResult<String, T>> kafkaCallback(String responseTopic, T requestAvroModel) {
        return new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error occurred when sending payment request to kafka topic", ex);

            }
            public void onSuccess(SendResult<String, T> result) {
                log.info("Successfully sent payment request to kafka topic");
            }
        };
    }
}
