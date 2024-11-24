package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductToShoppingCart(@RequestParam String username,
                                                    @RequestBody Map<UUID, Long> products) {
        return shoppingCartService.addProductsToShoppingCart(username, products);
    }

    @DeleteMapping
    public void deactivateShoppingCart(@RequestParam String username) {
        shoppingCartService.deactivateShoppingCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProductsFromCart(@RequestParam String username,
                                                  @RequestBody List<UUID> products) {
        return shoppingCartService.removeProducts(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantityInCart(@RequestParam String username, @RequestBody ChangeProductQuantityRequest request) {
        return shoppingCartService.changeProductQuantity(username, request);
    }

    @PostMapping("/booking")
    public BookedProductsDto bookingProductsInWarehouse(@RequestParam String username) {
        return shoppingCartService.bookProducts(username);
    }

}
