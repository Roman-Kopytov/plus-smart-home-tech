package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.consumer.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.service.ScenarioService;
import ru.yandex.practicum.service.SensorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {

    private final Consumer<String, HubEventAvro> consumer;
    private final KafkaProperties properties;
    private final SensorService sensorService;
    private final ScenarioService scenarioService;
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    @Override
    public void run() {
        log.info("HubEventProcessor processor started");
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(properties.getTopics().get("telemetry-hubs")));
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(properties.getPollTimeout());
                int count = 0;
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    sendToService(record);
                    manageOffsets(record, count, consumer);
                }
                consumer.commitAsync();
            }
        } catch (WakeupException e) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от хаба", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
            }
        }

    }


    private void sendToService(ConsumerRecord<String, HubEventAvro> record) {
        HubEventAvro event = record.value();
        switch (event.getPayload()) {
            case DeviceAddedEventAvro deviceAddedEvent ->
                    sensorService.addSensor(deviceAddedEvent.getId(), event.getHubId());
            case DeviceRemovedEventAvro deviceRemovedEvent ->
                    sensorService.removeSensor(deviceRemovedEvent.getId(), event.getHubId());
            case ScenarioAddedEventAvro scenarioAddedEvent ->
                    scenarioService.addScenario(scenarioAddedEvent, event.getHubId());
            case ScenarioRemovedEventAvro scenarioRemovedEvent ->
                    scenarioService.deleteScenario(scenarioRemovedEvent.getName(), event.getHubId());
            case null, default -> log.warn("Unknown event type: {}", event.getPayload().getClass().getName());
        }
    }

    private static void manageOffsets(ConsumerRecord<String, HubEventAvro> record, int count, Consumer<String, HubEventAvro> consumer) {

        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }
}