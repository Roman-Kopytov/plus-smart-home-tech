package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.*;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PutMapping
    public void addNewProductInWarehouse(@Valid @RequestBody NewProductInWarehouseRequest newProduct) {
        warehouseService.addNewProduct(newProduct);
    }

    @PostMapping("/shipped")
    public void shippedToDelivery(@Valid @RequestBody ShippedToDeliveryRequest deliveryRequest) {
        warehouseService.shippedToDelivery(deliveryRequest);
    }

    @PostMapping("/return")
    public void addReturnProductInWarehouse(Map<UUID, Long> products) {
        warehouseService.addReturnProduct(products);
    }

    @PostMapping("/check")
    public BookedProductsDto bookProductsInWarehouse(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkProductCount(shoppingCartDto);
    }

    @PostMapping("/assembly")
    public BookedProductsDto createAssembly(
            @Valid
            @RequestBody
            AssemblyProductsForOrderRequest assembly) {
        return warehouseService.createAssembly(assembly);
    }

    @PostMapping("/add")
    public void addProductToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest product) {
        warehouseService.increaseProductCount(product);
    }

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}
