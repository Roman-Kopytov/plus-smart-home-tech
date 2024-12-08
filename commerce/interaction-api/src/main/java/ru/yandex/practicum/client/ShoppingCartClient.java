package ru.yandex.practicum.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart")
public interface ShoppingCartClient {
    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam String username);

    @PutMapping
    ShoppingCartDto addProductToShoppingCart(@RequestParam String username,
                                             @RequestBody Map<UUID, Long> products);


    @DeleteMapping
    void deactivateShoppingCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProductsFromCart(@RequestParam String username,
                                           @RequestBody List<UUID> products);


    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantityInCart(@RequestParam String username,
                                                @RequestBody ChangeProductQuantityRequest request);

    @PostMapping("/booking")
    BookedProductsDto bookingProductsInWarehouse(@RequestParam String username);

}
