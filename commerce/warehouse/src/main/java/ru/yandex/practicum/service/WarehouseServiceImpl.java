package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.*;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.BookingProduct;
import ru.yandex.practicum.model.OrderBooking;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.BookingRepository;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final BookingRepository bookingRepository;
    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public void addNewProduct(NewProductInWarehouseRequest newProduct) {
        WarehouseProduct entity = warehouseMapper.toEntity(newProduct);
        warehouseRepository.save(entity);
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest) {
        OrderBooking orderBooking = bookingRepository.findById(deliveryRequest.getOrderId()).orElseThrow(() -> new NotFoundException("Booking not found"));
        orderBooking.setDeliveryId(deliveryRequest.getDeliveryId());
    }

    @Transactional
    @Override
    public void addReturnProduct(Map<UUID, Long> products) {
        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            WarehouseProduct product = getWarehouseProductFromRepository(entry.getKey());
            Long quantity = product.getQuantity();
            product.setQuantity(quantity + entry.getValue());
        }
    }

    @Transactional
    @Override
    public BookedProductsDto checkProductCount(ShoppingCartDto shoppingCartDto) {
        Map<UUID, Long> products = shoppingCartDto.getProducts();
        double deliveryWeight = 0;
        double deliveryVolume = 0;
        boolean fragile = false;
        List<WarehouseProduct> allById = warehouseRepository.findAllById(products.keySet());
        Map<UUID, WarehouseProduct> productById = allById.stream().collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));
        List<UUID> lowQuantityProduct = new ArrayList<>();
        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            Long needQuantity = entry.getValue();
            UUID productId = entry.getKey();
            Long existQuantity = productById.get(productId).getQuantity();
            if (needQuantity > existQuantity) {
                lowQuantityProduct.add(productId);
            }
            WarehouseProduct warehouseProduct = productById.get(productId);
            if (warehouseProduct.getFragile()) {
                fragile = true;
            }
            deliveryWeight = deliveryWeight + warehouseProduct.getWeight() * needQuantity;

            deliveryVolume = deliveryVolume + needQuantity
                    + (warehouseProduct.getDepth() * warehouseProduct.getWidth() * warehouseProduct.getHeight());
        }
        if (lowQuantityProduct.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Ошибка, товары " + lowQuantityProduct + " из корзины не находится в требуемом количестве на складе");
        }

        return BookedProductsDto.builder()
                .deliveryVolume(deliveryVolume)
                .deliveryWeight(deliveryWeight)
                .fragile(fragile)
                .build();
    }


    @Transactional
    @Override
    public BookedProductsDto createAssembly(AssemblyProductsForOrderRequest assembly) {
        List<BookingProduct> listProducts = new ArrayList<>();
        double deliveryWeight = 0;
        double deliveryVolume = 0;
        boolean fragile = false;
        List<WarehouseProduct> allById = warehouseRepository.findAllById(assembly.getProducts().keySet());
        Map<UUID, WarehouseProduct> productById = allById.stream().collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));
        OrderBooking booking = OrderBooking.builder()
                .orderId(assembly.getOrderId())
                .build();
        for (Map.Entry<UUID, Long> entry : assembly.getProducts().entrySet()) {
            Long needQuantity = entry.getValue();
            UUID productId = entry.getKey();
            Long existQuantity = productById.get(productId).getQuantity();
            if (needQuantity > existQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Ошибка, товар из корзины не находится в требуемом количестве на складе");
            }

            WarehouseProduct warehouseProduct = productById.get(productId);

            if (warehouseProduct.getFragile()) {
                fragile = true;
            }
            deliveryWeight = deliveryWeight + warehouseProduct.getWeight() * needQuantity;

            deliveryVolume = deliveryVolume + needQuantity
                    + (warehouseProduct.getDepth() * warehouseProduct.getWidth() * warehouseProduct.getHeight());


            warehouseProduct.setQuantity(existQuantity - needQuantity);
            warehouseRepository.save(warehouseProduct);

            listProducts.add(BookingProduct.builder()
                    .productId(productId)
                    .count(needQuantity)
                    .booking(booking)
                    .build());

        }
        booking.setProducts(listProducts);
        bookingRepository.save(booking);
        return BookedProductsDto.builder()
                .deliveryVolume(deliveryVolume)
                .deliveryWeight(deliveryWeight)
                .fragile(fragile)
                .build();

    }

    @Transactional
    @Override
    public void increaseProductCount(AddProductToWarehouseRequest product) {
        WarehouseProduct saveProduct = getWarehouseProductFromRepository(product.getProductId());
        saveProduct.setQuantity(saveProduct.getQuantity() + product.getQuantity());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

    private WarehouseProduct getWarehouseProductFromRepository(UUID productId) {
        return warehouseRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product by id: " + productId + " not found"));
    }
}
