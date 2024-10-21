package ru.practicum.controller;

import org.springframework.stereotype.Component;
import ru.practicum.config.KafkaTopicsProperties;
import ru.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro>{
    public ClimateSensorEventHandler(KafkaEventProducer producer, KafkaTopicsProperties kafkaTopics) {
        super(producer, kafkaTopics);
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEventProto event) {
        ClimateSensorEvent sensorEvent = event.getClimateSensorEvent();
       return ClimateSensorAvro.newBuilder()
                .setHumidity(sensorEvent.getHumidity())
                .setCo2Level(sensorEvent.getCo2Level())
                .setTemperatureC(sensorEvent.getTemperatureC())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }
}
