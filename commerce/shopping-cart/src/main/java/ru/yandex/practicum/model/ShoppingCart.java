package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shoppingCartId;
    @Column(name = "username")
    private String username;
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ShoppingCartStatus status;
}
