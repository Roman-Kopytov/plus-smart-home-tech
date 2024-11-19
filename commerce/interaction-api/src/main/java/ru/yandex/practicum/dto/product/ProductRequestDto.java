package ru.yandex.practicum.dto.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private String productId;
    @NotNull
    private String productName;
    @NotNull
    private String description;
    private String imageSrc;
    @NotNull
    private QuantityState quantityState;
    @NotNull
    private ProductState productState;

    @NotNull
    @Min(1)
    @Max(5)
    private Double rating;

    private ProductCategory productCategory;
    @NotNull
    @Min(value = 1)
    private Double price;
}
