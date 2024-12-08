package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void addNewProduct(NewProductInWarehouseRequest newProduct);

    void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest);

    void addReturnProduct(Map<UUID, Long> products);

    BookedProductsDto checkProductCount(ShoppingCartDto shoppingCartDto);

    BookedProductsDto createAssembly(AssemblyProductsForOrderRequest assembly);

    void increaseProductCount(AddProductToWarehouseRequest product);

    AddressDto getWarehouseAddress();


}
