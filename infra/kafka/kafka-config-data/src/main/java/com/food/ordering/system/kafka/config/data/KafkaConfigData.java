package com.food.ordering.system.kafka.config.data;

import lombok.Data;

import java.beans.ConstructorProperties;

@Data
@Configuration
@ConstructorProperties(prefix= "kafka-config")
public class KafkaConfigData {
    private String bootstrapServers;
    private String schemaRegistryUrl;
    private String schemaRegistryUrlKey;
    private Integer numPartitions;
    private Integer replicationFactor;
}
