package ru.yandex.practicum.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetProductQuantityStateRequest {
    private String productId;
    private QuantityState quantityState;
}
