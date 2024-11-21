package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductForOrderFromShoppingCartRequest;

import java.util.Map;

@FeignClient(name = "warehouse")
public interface WarehouseClient {
    @PostMapping("/api/v1/warehouse/booking")
    BookedProductsDto bookProductsInWarehouse(ShoppingCartDto shoppingCartDto);

    @PostMapping("/api/v1/warehouse/assembly")
    BookedProductsDto createAssembly(AssemblyProductForOrderFromShoppingCartRequest assembly);

    @PostMapping("/api/v1/warehouse/return")
    void addReturnProductInWarehouse(Map<String, Long> products);


}
