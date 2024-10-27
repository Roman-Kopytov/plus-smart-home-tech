package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class AggregationStarter {
    private final Consumer<String, SensorEventAvro> consumer;
    private final Producer<String, SensorEventAvro> producer;
    private final KafkaTopicsProperties kafkaTopics;
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(kafkaTopics.getTelemetrySensors()));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    handleRecord(record);
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException | InterruptedException ignores) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                producer.flush();
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private static void manageOffsets(ConsumerRecord<String, SensorEventAvro> record, int count, Consumer<String, SensorEventAvro> consumer) {

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

    private void handleRecord(ConsumerRecord<String, SensorEventAvro> record) throws InterruptedException {
        SensorEventAvro event = record.value();
        Optional<SensorsSnapshotAvro> sensorsSnapshotAvro = updateState(event);
        if (sensorsSnapshotAvro.isPresent()) {
            SensorsSnapshotAvro sensorsSnapshot = sensorsSnapshotAvro.get();

            String topic = kafkaTopics.getTelemetrySnapshots();
            String key = sensorsSnapshot.getHubId();
            send(topic, key, event);

        }
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String eventId = event.getId();
        String hubId = event.getHubId();
        Object payload = event.getPayload();
        Instant timestamp = event.getTimestamp();
        SensorsSnapshotAvro snapshot = snapshots.get(hubId);
        if (snapshot == null) {

            snapshot = new SensorsSnapshotAvro(hubId, timestamp, new HashMap<>());
            snapshots.put(hubId, snapshot);
        }

        SensorStateAvro oldState = snapshot.getSensorsState().get(eventId);
        if (oldState != null) {

            if (oldState.getTimestamp().isAfter(timestamp) ||
                    oldState.getData().equals(payload)) {

                return Optional.empty();
            }
        }

        SensorStateAvro newState = new SensorStateAvro(timestamp, payload);
        snapshot.getSensorsState().put(eventId, newState);
        snapshot.setTimestamp(timestamp);

        return Optional.of(snapshot);
    }

    private void send(String topic, String key, SensorEventAvro event) {
        ProducerRecord<String, SensorEventAvro> record =
                new ProducerRecord<>(topic, 1, Instant.now().getEpochSecond(), key, event);

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Ошибка при отправке сообщения в Kafka, topic: {}, key: {}", topic, key, exception);
            } else {
                log.info("Сообщение успешно отправлено в Kafka, topic: {}, partition: {}, offset: {}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }


}