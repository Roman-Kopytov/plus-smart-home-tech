package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.product.*;

import java.util.List;

public interface BaseProductService {

    ProductFullDto createProduct(ProductRequestDto productRequestDto);

    ProductFullDto updateProduct(ProductRequestDto productRequestDto);

    boolean removeProduct(String productId);

    ProductFullDto getProduct(String productId);

    boolean setProductQuantityState(SetProductQuantityStateRequest productQuantityStateRequest);

    List<ProductFullDto> getProducts(ProductCategory category, Pageable pageable);

}
