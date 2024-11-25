package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;

    public PayOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                         OrderServiceConfigData orderServiceConfigData,
                                         KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
    }
    @Override
    public void publish(OrderPaidEvent event) {
        String orderId = event.getOrder().getId().getValue().toString();
        log.info("Receive OrderPaymentEvent for order with Id: {}", orderId);

        RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper.orderPaidEventToRestaurantApprovalRequestAvroModel(event);
        kafkaProducer.send(
                orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                orderId,
                restaurantApprovalRequestAvroModel,
                kafkaCallback(orderServiceConfigData.getRestaurantApprovalResponseTopicName(), restaurantApprovalRequestAvroModel)
        );
    }


    private ListenableFutureCallback<SendResult<String, RestaurantApprovalRequestAvroModel>> kafkaCallback(String paymentResponseTopicName, RestaurantApprovalRequestAvroModel paymentRequestAvroModel) {
        return new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error occurred when sending payment request to kafka topic", ex);

            }
            public void onSuccess(SendResult<String, RestaurantApprovalRequestAvroModel> result) {
                log.info("Successfully sent payment request to kafka topic");
            }
        };
    }
}
