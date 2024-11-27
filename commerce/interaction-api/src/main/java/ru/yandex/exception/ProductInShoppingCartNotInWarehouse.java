package ru.yandex.exception;

public class ProductInShoppingCartNotInWarehouse extends RuntimeException{
    public ProductInShoppingCartNotInWarehouse(String message) {
        super(message);
    }
}
