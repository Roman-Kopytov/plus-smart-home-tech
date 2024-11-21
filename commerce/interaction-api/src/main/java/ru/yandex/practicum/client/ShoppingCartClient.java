package ru.yandex.practicum.client;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-cart")
public interface ShoppingCartClient {
}
