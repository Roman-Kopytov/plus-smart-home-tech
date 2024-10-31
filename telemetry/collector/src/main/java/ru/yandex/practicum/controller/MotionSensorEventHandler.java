package ru.yandex.practicum.controller;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaTopicsProperties;
import ru.yandex.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {
    public MotionSensorEventHandler(KafkaEventProducer producer, KafkaTopicsProperties kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEventProto event) {
        MotionSensorEvent sensorEvent = event.getMotionSensorEvent();
        return MotionSensorAvro.newBuilder()
                .setMotion(sensorEvent.getMotion())
                .setLinkQuality(sensorEvent.getLinkQuality())
                .setVoltage(sensorEvent.getVoltage())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }
}
