package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.product.ProductFullDto;
import ru.yandex.practicum.dto.product.SetProductQuantityStateRequest;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient {
    @PostMapping("/api/v1/shopping-store/quantityState")
    boolean setProductQuantityState
            (@RequestBody SetProductQuantityStateRequest setProductQuantityStateRequest);

    @GetMapping("/{productId}")
    ProductFullDto getProduct(@PathVariable(name = "productId") String productId);

}
