package ru.yandex.practicum.consumer;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaProperties {
    private String bootstrapServers;
    private long pollDuration;
    private int messageFixTime;
    private String schemaRegistryUrl;
    private String keyDeserializer;
    private Map<String, String> valueDeserializers;
    private Map<String, String> groupId;
    private Map<String, String> topics;

}
