package ru.yandex.practicum.model;


import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dto.product.ProductCategory;
import ru.yandex.practicum.dto.product.ProductState;
import ru.yandex.practicum.dto.product.QuantityState;

@Table(name = "products",schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;
    private String productName;
    private String description;
    private String imageSrc;

    @Enumerated(value = EnumType.STRING)
    private QuantityState quantityState;

    @Enumerated(value = EnumType.STRING)
    private ProductState productState;

    private Double rating;

    @Enumerated(value = EnumType.STRING)
    private ProductCategory productCategory;

    private Double price;
}


