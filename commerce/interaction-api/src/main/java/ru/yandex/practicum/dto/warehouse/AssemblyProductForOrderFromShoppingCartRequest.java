package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssemblyProductForOrderFromShoppingCartRequest {
    @NotNull
    private String shoppingCartId;
    @NotNull
    private String orderId;
}
