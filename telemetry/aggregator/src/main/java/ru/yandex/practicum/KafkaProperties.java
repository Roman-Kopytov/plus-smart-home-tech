package ru.yandex.practicum;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaProperties {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private String valueDeserializer;
    private String keyDeserializer;
    private String keySerializer;
    private String valueSerializer;
    private long pollDuration;
    private int messageFixTime;

}
