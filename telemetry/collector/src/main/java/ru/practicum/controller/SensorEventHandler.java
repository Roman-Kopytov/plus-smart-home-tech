package ru.practicum.controller;

import ru.practicum.model.sensor.SensorEvent;

public interface SensorEventHandler {
    void handle(SensorEvent sensorEvent);

}
