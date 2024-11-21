package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.product.*;
import ru.yandex.practicum.service.BaseProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController {

    private final BaseProductService productService;

    @GetMapping
    public List<ProductFullDto> getProducts(@RequestParam(name = "category") ProductCategory category,
                                            Pageable pageable) {
        return productService.getProducts(category, pageable);
    }


    @PutMapping
    public ProductFullDto createProduct(@Valid @RequestBody ProductRequestDto productDto) {
        return productService.createProduct(productDto);
    }

    @PostMapping
    public ProductFullDto updateProduct(@Valid @RequestBody ProductRequestDto productDto) {
        return productService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(@RequestBody String productId) {
        return productService.removeProduct(productId);
    }

    @PostMapping("/quantityState")
    public boolean setProductQuantityState
            (SetProductQuantityStateRequest productQuantityStateRequest) {
        return productService.setProductQuantityState(productQuantityStateRequest);
    }

    @GetMapping("/{productId}")
    public ProductFullDto getProduct(@PathVariable(name = "productId") String productId) {
        return productService.getProduct(productId);
    }


}
