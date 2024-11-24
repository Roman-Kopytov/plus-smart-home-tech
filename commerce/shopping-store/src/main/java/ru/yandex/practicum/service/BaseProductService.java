package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.product.*;

import java.util.List;
import java.util.UUID;

public interface BaseProductService {

    ProductFullDto createProduct(ProductRequestDto productRequestDto);

    ProductFullDto updateProduct(ProductRequestDto productRequestDto);

    boolean removeProduct(UUID productId);

    ProductFullDto getProduct(UUID productId);

    boolean setProductQuantityState(SetProductQuantityStateRequest productQuantityStateRequest);

    List<ProductFullDto> getProducts(ProductCategory category, Pageable pageable);

}
