package ru.yandex.practicum;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicsProperties {

    private String telemetrySensors;
    private String telemetrySnapshots;
}