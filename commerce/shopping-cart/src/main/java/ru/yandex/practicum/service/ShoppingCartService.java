package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;

import java.util.Map;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProductsToShoppingCart(String username, Map<String, Long> newProducts);

    void deactivateShoppingCart(String username);

    ShoppingCartDto removeProducts(String username, Map<String, Long> newProducts);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest);

    BookedProductsDto bookProducts(String username);

}
