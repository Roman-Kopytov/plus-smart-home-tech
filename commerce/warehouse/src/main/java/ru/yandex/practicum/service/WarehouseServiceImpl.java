package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductForOrderFromShoppingCartRequest;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.mapeer.BookingMapper;
import ru.yandex.practicum.mapeer.WarehouseMapper;
import ru.yandex.practicum.model.Booking;
import ru.yandex.practicum.model.BookingProduct;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.BookingRepository;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final OrderRepository orderRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public void addNewProduct(NewProductInWarehouseRequest newProduct) {
        WarehouseProduct entity = warehouseMapper.toEntity(newProduct);
        warehouseRepository.save(entity);
    }

    @Override
    public void addReturnProduct(Map<String, Long> products) {

    }

    @Transactional
    @Override
    public BookedProductsDto bookProducts(ShoppingCartDto shoppingCartDto) {
        List<BookingProduct> listProducts = new ArrayList<>();
        List<WarehouseProduct> allById = warehouseRepository.findAllById(shoppingCartDto.getProducts().keySet());
        Map<UUID, WarehouseProduct> productById = allById.stream().collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));
        Booking booking = Booking.builder()
                .shoppingCartId(shoppingCartDto.getShoppingCartId())
                .build();
        for (Map.Entry<UUID, Long> entry : shoppingCartDto.getProducts().entrySet()) {
            Long needQuantity = entry.getValue();
            UUID productId = entry.getKey();
            Long existQuantity = productById.get(productId).getQuantity();
            if (needQuantity > existQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Ошибка, товар из корзины не находится в требуемом количестве на складе");
            }
            WarehouseProduct warehouseProduct = productById.get(productId);
            warehouseProduct.setQuantity(existQuantity - needQuantity);
            warehouseRepository.save(warehouseProduct);

            listProducts.add(BookingProduct.builder()
                    .productId(productId)
                    .count(needQuantity)
                    .booking(booking)
                    .build());
        }
        booking.setProducts(listProducts);
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toDto(saved);
    }

    @Transactional
    @Override
    public BookedProductsDto createAssembly(AssemblyProductForOrderFromShoppingCartRequest assembly) {
        Booking booking = bookingRepository.findByShoppingCartId(assembly.getShoppingCartId());
        double deliveryWeight = 0;
        double deliveryVolume = 0;
        boolean fragile = false;
        List<UUID> productIds = booking.getProducts().stream().map(BookingProduct::getProductId).collect(Collectors.toList());
        List<WarehouseProduct> productFromWarehouse = warehouseRepository.findAllById(productIds);
        Map<UUID, WarehouseProduct> productById = productFromWarehouse.stream().collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));
        for (BookingProduct product : booking.getProducts()) {
            WarehouseProduct warehouseProduct = productById.get(product.getProductId());
            Long count = product.getCount();
            if (warehouseProduct.getFragile()) {
                fragile = true;
            }
            deliveryWeight = deliveryWeight + warehouseProduct.getWeight() + count;
            double volume = count + (warehouseProduct.getDepth() * warehouseProduct.getWidth() * warehouseProduct.getHeight());
            deliveryVolume = deliveryVolume + volume;
        }
        BookedProductsDto.builder()
                .deliveryVolume(deliveryVolume)
                .deliveryWeight(deliveryWeight)
                .fragile(fragile)
                .build();

        return null;
    }

    @Transactional
    @Override
    public void increaseProductCount(AddProductToWarehouseRequest product) {
        WarehouseProduct saveProduct = getWarehouseProductFromRepository(product.getProductId());
        saveProduct.setQuantity(saveProduct.getQuantity() + product.getQuantity());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return new AddressDto("Russia", "Yandex", "First", "10", "12");
    }

    private WarehouseProduct getWarehouseProductFromRepository(UUID productId) {
        WarehouseProduct product = warehouseRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product by id: " + productId + " not found"));
        return product;
    }
}
