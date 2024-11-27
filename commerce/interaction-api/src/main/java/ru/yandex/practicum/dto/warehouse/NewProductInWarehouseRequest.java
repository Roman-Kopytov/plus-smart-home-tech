package ru.yandex.practicum.dto.warehouse;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductInWarehouseRequest {
    @NotNull
    private String productId;
    private boolean fragile;
    @NotNull
    private DimensionDto dimension;
    @NotNull
    @Min(1)
    private Double weight;
}
