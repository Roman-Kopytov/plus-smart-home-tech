package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDto planDelivery(DeliveryDto deliveryDto);

    void successfulDelivery(UUID orderId);

    void pickedToDelivery(UUID orderId);

    void failedDelivery(UUID orderId);

    Double calculateFullDeliveryCost(OrderDto orderDto);
}
