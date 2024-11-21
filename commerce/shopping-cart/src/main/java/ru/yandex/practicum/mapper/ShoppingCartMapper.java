package ru.yandex.practicum.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ShoppingCartMapper {
    //TODO
    @Mapping(source = "id", target = "shoppingCartId")
    @Mapping(target = "products", expression = "java(mapProductsToMap(cart.getProducts()))")
    ShoppingCartDto toDto(ShoppingCart cart);

    default Map<String, Long> mapProductsToMap(List<Product> products) {
        return products.stream()
                .collect(Collectors.toMap(Product::getProductId, Product::getCount));
    }

    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);
}
