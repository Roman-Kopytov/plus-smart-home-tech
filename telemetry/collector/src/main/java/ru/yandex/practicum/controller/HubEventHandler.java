package ru.yandex.practicum.controller;

import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.HubEventType;

public interface HubEventHandler {
    void handle(HubEvent hubEvent);

    HubEventType getMessageType();

}
