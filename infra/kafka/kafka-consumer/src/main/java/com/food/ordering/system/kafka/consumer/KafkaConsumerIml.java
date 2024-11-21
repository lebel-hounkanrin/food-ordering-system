package com.food.ordering.system.kafka.consumer;

import org.apache.avro.specific.SpecificRecord;

import java.util.List;

public class KafkaConsumerIml<T extends SpecificRecord> implements KafkaConsumer {
    @Override
    public void receive(List messages, List key, List partitions, List offsets) {

    }
}
