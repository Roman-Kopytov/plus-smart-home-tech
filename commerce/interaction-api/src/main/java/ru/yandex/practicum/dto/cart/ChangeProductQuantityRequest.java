package ru.yandex.practicum.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeProductQuantityRequest {
    private UUID productId;
    private long newQuantity;
}
