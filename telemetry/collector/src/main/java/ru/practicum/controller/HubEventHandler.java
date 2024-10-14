package ru.practicum.controller;

import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.hub.HubEventType;

public interface HubEventHandler {
    void handle(HubEvent hubEvent);

    HubEventType getMessageType();

}
