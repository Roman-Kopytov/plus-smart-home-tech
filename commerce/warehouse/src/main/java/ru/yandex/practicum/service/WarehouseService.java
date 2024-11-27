package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductForOrderFromShoppingCartRequest;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

import java.util.Map;

public interface WarehouseService {
    void addNewProduct(NewProductInWarehouseRequest newProduct);

    void addReturnProduct(Map<String, Long> products);

    BookedProductsDto bookProducts(ShoppingCartDto shoppingCartDto);

    BookedProductsDto createAssembly(AssemblyProductForOrderFromShoppingCartRequest assembly);

    void increaseProductCount(AddProductToWarehouseRequest product);

    AddressDto getWarehouseAddress();


}
