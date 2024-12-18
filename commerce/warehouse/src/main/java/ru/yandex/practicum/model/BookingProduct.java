package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "booking_product")
public class BookingProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "product_id")
    private UUID productId;
    @Column(name = "count")
    private Long count;
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private OrderBooking booking;
}
