package ru.yandex.practicum.mapeer;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.model.Booking;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface BookingMapper {
    BookedProductsDto toDto(Booking booking);
}
