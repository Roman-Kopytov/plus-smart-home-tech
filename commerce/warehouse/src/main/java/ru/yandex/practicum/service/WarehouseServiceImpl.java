package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductForOrderFromShoppingCartRequest;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.mapeer.WarehouseMapper;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    public void addNewProduct(NewProductInWarehouseRequest newProduct) {
        WarehouseProduct entity = warehouseMapper.toEntity(newProduct);
        warehouseRepository.save(entity);
    }

    @Override
    public void addReturnProduct(Map<String, Long> products) {

    }

    @Override
    public BookedProductsDto bookProducts(ShoppingCartDto shoppingCartDto) {
        return null;
    }

    @Override
    public BookedProductsDto createAssembly(AssemblyProductForOrderFromShoppingCartRequest assembly) {
        return null;
    }

    @Override
    public void increaseProductCount(AddProductToWarehouseRequest product) {
        WarehouseProduct saveProduct = getWarehouseProductFromRepository(product.getProductId());
        saveProduct.setQuantity(saveProduct.getQuantity() + product.getQuantity());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return null;
    }

    private WarehouseProduct getWarehouseProductFromRepository(String productId) {
        WarehouseProduct product = warehouseRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product by id: " + productId + " not found"));
        return product;
    }
}
