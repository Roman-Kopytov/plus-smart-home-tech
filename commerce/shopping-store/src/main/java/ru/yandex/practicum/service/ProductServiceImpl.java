package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.product.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements BaseProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductFullDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.toProduct(productRequestDto);
        ProductFullDto productFullDto = productMapper.toProductFullDto(productRepository.save(product));

        return productFullDto;
    }

    @Override
    @Transactional
    public ProductFullDto updateProduct(ProductRequestDto productRequestDto) {
        Product savedProduct = getProductFromRepository(productRequestDto.getProductId());
        Product newProduct = productMapper.toProduct(productRequestDto);
        if (newProduct.getProductName() != null) {
            savedProduct.setProductName(newProduct.getProductName());
        }
        if (newProduct.getProductState() != null) {
            savedProduct.setProductState(newProduct.getProductState());
        }
        if (newProduct.getDescription() != null) {
            savedProduct.setDescription(newProduct.getDescription());
        }
        if (newProduct.getPrice() != null) {
            savedProduct.setPrice(newProduct.getPrice());
        }
        if (newProduct.getImageSrc() != null) {
            savedProduct.setImageSrc(newProduct.getImageSrc());
        }
        if (newProduct.getProductCategory() != null) {
            savedProduct.setProductCategory(newProduct.getProductCategory());
        }
        if (newProduct.getQuantityState() != null) {
            savedProduct.setQuantityState(newProduct.getQuantityState());
        }
        return productMapper.toProductFullDto(productRepository.save(savedProduct));
    }

    @Override
    @Transactional
    public boolean removeProduct(String productId) {
//        TODO
        if (getProductFromRepository(productId) != null) {
            productRepository.deleteById(productId);

            return true;
        } else return false;
    }

    @Override
    public ProductFullDto getProduct(String productId) {
        return productMapper.toProductFullDto(getProductFromRepository(productId));
    }

    @Override
    @Transactional
    public boolean setProductQuantityState(SetProductQuantityStateRequest productQuantityStateRequest) {
        Product savedProduct = getProductFromRepository(productQuantityStateRequest.getProductId());
        savedProduct.setQuantityState(productQuantityStateRequest.getQuantityState());
        return true;
    }

    @Override
    public List<ProductFullDto> getProducts(ProductCategory category, Pageable pageable) {

        Sort sort = Sort.by(Sort.Direction.ASC, String.valueOf(pageable.getSort().getFirst()));
        PageRequest pageRequest = PageRequest.of(pageable.getPage(), pageable.getSize(), sort);
        return productRepository.findAllByProductCategory(category, pageRequest)
                .stream()
                .map(p -> productMapper.toProductFullDto(p))
                .toList();
    }

    private Product getProductFromRepository(String productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product with id: " + productId + " not found"));
    }
}
