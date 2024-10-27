package ru.yandex.practicum;

import lombok.Getter;
import lombok.Setter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.kafka.producer")
public class KafkaProducerConfig<T extends SpecificRecordBase> {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;

    @Bean
    public Producer<String, T> kafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", keySerializer);
        props.put("value.serializer", valueSerializer);
        return new KafkaProducer<>(props);
    }
}
