package ru.practicum.controller;

import org.springframework.stereotype.Component;
import ru.practicum.config.KafkaTopicsProperties;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {

    public LightSensorEventHandler(KafkaEventProducer producer, KafkaTopicsProperties kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEventProto event) {
        LightSensorEvent sensorEvent = event.getLightSensorEvent();
        return LightSensorAvro.newBuilder()
                .setLinkQuality(sensorEvent.getLinkQuality())
                .setLuminosity(sensorEvent.getLuminosity())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }
}
