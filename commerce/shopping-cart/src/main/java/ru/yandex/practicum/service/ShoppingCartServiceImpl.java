package ru.yandex.practicum.service;

import feign.FeignException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.cart.BookedProductsDto;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.product.ProductFullDto;
import ru.yandex.practicum.dto.product.ProductState;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartStatus;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingStoreClient shoppingStoreClient;
    private final WarehouseClient warehouseClient;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        return null;
    }

    @Override
    public ShoppingCartDto addProductsToShoppingCart(String username, Map<String, Long> newProducts) {
        ShoppingCart cart;
        try {
            cart = getCartFromRepositoryByName(username);
        } catch (NotFoundException e) {
            cart = new ShoppingCart();
        }
        List<Product> products = new ArrayList<>();
        for (Map.Entry<String, Long> entry : newProducts.entrySet()) {
            String productId = entry.getKey();
            try {
                ProductFullDto product = shoppingStoreClient.getProduct(productId);
                if (product.getProductState() == ProductState.ACTIVE && entry.getValue() >= 1) {
                    products.add(Product.builder()
                            .productId(productId)
                            .count(entry.getValue())
                            .build());
                }
            } catch (FeignException e) {
                if (e.status() == 404) {
                    System.out.println("Product not found");
                } else {
                    System.out.println("Feign error: " + e.status());
                }
            }
        }
        cart.setProducts(products);
        ShoppingCart save = shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(save);

    }

    private ShoppingCart getCartFromRepositoryByName(String username) {
        ShoppingCart cart = shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Cart by username: " + username + " not found"));
        return cart;
    }

    @Transactional
    @Override
    public void deactivateShoppingCart(String username) {
        ShoppingCart savedCart = getCartFromRepositoryByName(username);
        savedCart.setStatus(ShoppingCartStatus.DISABLE);
    }

    @Override
    public ShoppingCartDto removeProducts(String username, Map<String, Long> newProducts) {
        ShoppingCart cart = getCartFromRepositoryByName(username);
        for (Map.Entry<String, Long> entry : newProducts.entrySet()) {

        }
        return null;
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest) {
        return null;
    }

    @Override
    public BookedProductsDto bookProducts(String username) {
        return null;
    }
}
