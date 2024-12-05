package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.delivery.DeliveryState;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AddressMapper addressMapper;
    private final DeliveryMapper deliveryMapper;
    private final WarehouseClient warehouseClient;
    private final OrderClient orderClient;
    private static final String ADDRESS_1 = "ADDRESS_1";
    private static final String ADDRESS_2 = "ADDRESS_2";

    @Override
    @Transactional
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = Delivery.builder()
                .fromAddress(addressMapper.toAddress(deliveryDto.getFromAddress()))
                .toAddress(addressMapper.toAddress(deliveryDto.getToAddress()))
                .deliveryState(deliveryDto.getDeliveryState())
                .build();
        Delivery save = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(save);
    }

    @Transactional
    @Override
    public void successfulDelivery(UUID orderId) {
        Delivery delivery = getDeliveryFromRepository(orderId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        try {
            orderClient.deliveryOrder(orderId);
        } catch (FeignException e) {
            log.warn("Failed to change status order: {}", orderId, e);
            throw e;
        }

    }

    @Override
    public void pickedToDelivery(UUID orderId) {
        Delivery delivery = getDeliveryFromRepository(orderId);
        ShippedToDeliveryRequest deliveryRequest = new ShippedToDeliveryRequest(orderId, delivery.getDeliveryId());
        try {
            warehouseClient.shippedToDelivery(deliveryRequest);
        } catch (FeignException e) {
            log.warn("Failed to change status order: {}", orderId, e);
            throw e;
        }
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        ShippedToDeliveryRequest request = ShippedToDeliveryRequest.builder()
                .orderId(delivery.getOrderId())
                .deliveryId(delivery.getDeliveryId())
                .build();
        warehouseClient.shippedToDelivery(request);
    }

    @Transactional
    @Override
    public void failedDelivery(UUID orderId) {
//        Здесь и не только не очень понтяно. Если мы поставим статус доставки failed, но поймаем ошибку в orderClient
//        то транзакция откатится, и мы не запомним этот статус
        Delivery delivery = getDeliveryFromRepository(orderId);
        try {
            orderClient.deliveryFailedOrder(orderId);
        } catch (FeignException e) {
            log.warn("Failed to change status order: {}", orderId, e);
            throw e;
        }
        delivery.setDeliveryState(DeliveryState.FAILED);
    }

    @Override
    public Double calculateFullDeliveryCost(OrderDto orderDto) {
        Delivery delivery = getDeliveryFromRepository(orderDto.getDeliveryId());
        Double deliveryCost = 5.0;

        if (delivery.getFromAddress().getCity().equals(ADDRESS_1)) {
            deliveryCost = deliveryCost * 1;
        } else {
            deliveryCost = deliveryCost * 2;
        }

        if (orderDto.getFragile()) {
            deliveryCost = deliveryCost * 1.2;
        }
        deliveryCost = deliveryCost + orderDto.getDeliveryWeight() * 0.3;
        deliveryCost = deliveryCost + orderDto.getDeliveryVolume() * 0.2;
        if (!delivery.getToAddress().getStreet().equals(delivery.getFromAddress().getStreet())) {
            deliveryCost = deliveryCost * 1.2;
        }
        delivery.setDeliveryVolume(orderDto.getDeliveryVolume());
        delivery.setDeliveryWeight(orderDto.getDeliveryWeight());
        delivery.setFragile(orderDto.getFragile());

        return deliveryCost;
    }

    private Delivery getDeliveryFromRepository(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId).orElseThrow(() -> new NoDeliveryFoundException("No delivery found with id " + orderId));
    }
}
