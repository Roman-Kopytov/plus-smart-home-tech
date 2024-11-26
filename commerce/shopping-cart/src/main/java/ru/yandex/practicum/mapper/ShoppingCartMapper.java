package ru.yandex.practicum.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.model.ProductCart;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ShoppingCartMapper {
    @Mapping(source = "shoppingCartId", target = "shoppingCartId")
    @Mapping(target = "products", expression = "java(mapProductsToMap(cart.getProducts()))")
    ShoppingCartDto toDto(ShoppingCart cart);

    default Map<UUID, Long> mapProductsToMap(List<ProductCart> products) {
        return products.stream()
                .collect(Collectors.toMap(ProductCart::getProductId, ProductCart::getCount));
    }

}
