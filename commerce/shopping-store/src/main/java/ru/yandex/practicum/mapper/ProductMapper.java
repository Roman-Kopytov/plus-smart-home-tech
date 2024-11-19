package ru.yandex.practicum.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.product.ProductFullDto;
import ru.yandex.practicum.dto.product.ProductRequestDto;
import ru.yandex.practicum.model.Product;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ProductMapper {

    @Mapping(target = "productId", ignore = true)
    Product toProduct(ProductRequestDto productDto);

    ProductFullDto toProductFullDto(Product product);
}
