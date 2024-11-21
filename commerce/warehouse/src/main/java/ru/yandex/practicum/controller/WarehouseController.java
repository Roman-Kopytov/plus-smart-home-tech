package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductForOrderFromShoppingCartRequest;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PutMapping
    public void addNewProductInWarehouse(@Valid @RequestBody NewProductInWarehouseRequest newProduct) {
        return warehouseService.addNewProduct(newProduct);
    }

    @PostMapping("/return")
    public void addReturnProductInWarehouse(Map<String, Long> products) {
        warehouseService.addReturnProduct(products);
    }

    @PostMapping("/booking")
    public BookedProductsDto bookProductsInWarehouse(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        return warehouseService.bookProducts(shoppingCartDto);
    }

    @PostMapping("/assembly")
    public BookedProductsDto createAssembly(
            @Valid
            @RequestBody
            AssemblyProductForOrderFromShoppingCartRequest assembly) {
        return warehouseService.createAssembly(assembly);
    }

    @PostMapping("/add")
    public boolean addProductToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest product) {
        return warehouseService.increaseProductCount(product);
    }

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}
