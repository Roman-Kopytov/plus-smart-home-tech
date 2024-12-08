package ru.yandex.practicum.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderProduct;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface OrderMapper {
    @Mapping(source = "shoppingCartId", target = "shoppingCartId")
    @Mapping(target = "products", expression = "java(mapProductsToMap(order.getProducts()))")
    OrderDto toDto(Order order);

    default Map<UUID, Long> mapProductsToMap(List<OrderProduct> products) {
        return products.stream()
                .collect(Collectors.toMap(OrderProduct::getProductId, OrderProduct::getCount));
    }
}
