package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.model.sensor.SensorEvent;
import org.apache.avro.specific.SpecificRecordBase;

@RequiredArgsConstructor
@Slf4j
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {

    protected final KafkaEventProducer producer;
    protected final KafkaTopics kafkaTopics;

    protected abstract T mapToAvro(SensorEvent event);

    @Override
    public void handle(SensorEvent event) {
        T avroEvent = mapToAvro(event);
        String topic = kafkaTopics.getTelemetrySensors();

        log.info("Отправка события {} в топик {}", getMessageType(), topic);
        producer.send(topic, event.getId(), avroEvent);
    }
}
