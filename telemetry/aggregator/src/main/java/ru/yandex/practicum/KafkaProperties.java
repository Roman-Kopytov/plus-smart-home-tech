package ru.yandex.practicum;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aggregator.kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private ProducerProperties producer;
    private ConsumerProperties consumer;

    @Getter
    @Setter
    public static class ProducerProperties {
        private Map<String, String> properties;
        private String topic;
    }

    @Getter
    @Setter
    public static class ConsumerProperties {
        private Map<String, String> properties;
        private String topic;
        private int pollDuration;
        private int messageFixTime;
    }
}
