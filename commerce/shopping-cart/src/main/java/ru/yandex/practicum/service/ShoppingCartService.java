package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProductsToShoppingCart(String username, Map<UUID, Long> newProducts);

    void deactivateShoppingCart(String username);

    ShoppingCartDto removeProducts(String username, List<UUID> newProducts);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest);

    BookedProductsDto bookProducts(String username);

}
