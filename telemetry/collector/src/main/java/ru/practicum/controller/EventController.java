package ru.practicum.controller;

import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.model.sensor.SensorEventType;

import java.util.Map;

public class EventController {
    private final Map<SensorEventType, SensorEvent> eventMap;
}
