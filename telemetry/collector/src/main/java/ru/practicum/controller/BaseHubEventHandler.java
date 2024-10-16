package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.service.KafkaEventProducer;
import ru.practicum.config.KafkaTopicsProperties;
import ru.practicum.model.hub.HubEvent;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    protected final KafkaEventProducer producer;
    protected final KafkaTopicsProperties kafkaTopics;

    protected abstract T mapToAvro(HubEvent event);

    @Override
    public void handle(HubEvent event) {
        T avroEvent = mapToAvro(event);
        String topic = kafkaTopics.getTelemetryHubs();

        log.info("Отправка события {} в топик {}", getMessageType(), topic);
        producer.send(topic, event.getHubId(), avroEvent);
    }
}