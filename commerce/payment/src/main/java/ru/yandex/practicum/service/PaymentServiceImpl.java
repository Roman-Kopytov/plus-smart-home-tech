package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.OrderState;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.payment.PaymentStatus;
import ru.yandex.practicum.dto.product.ProductFullDto;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ShoppingStoreClient shoppingStoreClient;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        PaymentStatus status = PaymentStatus.PENDING;
        if (orderDto.getState().equals(OrderState.PAID)) {
            status = PaymentStatus.SUCCESS;
        } else if (orderDto.getState().equals(OrderState.PAYMENT_FAILED)) {
            status = PaymentStatus.FAILED;
        }
        Payment payment = Payment.builder()
                .deliveryPrice(orderDto.getDeliveryPrice())
                .totalPrice(orderDto.getTotalPrice())
                .productPrice(orderDto.getProductPrice())
                .status(status)
                .build();
        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDto(saved);
    }

    @Override
    public Double calculateTotalCost(OrderDto orderDto) {
        Payment payment = getFromRepository(orderDto.getOrderId());

        return 0.0;
    }

    @Override
    public void successPayment(UUID orderId) {

    }


    @Transactional
    @Override
    public Double calculateProductCost(OrderDto orderDto) {
        Payment payment = getFromRepository(orderDto.getOrderId());
        Map<UUID, Long> products = orderDto.getProducts();
        Double productCost = 0.0;
        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            ProductFullDto product = shoppingStoreClient.getProduct(entry.getKey());
            Double price = product.getPrice();
            productCost = productCost + price * entry.getValue();
        }
        payment.setProductPrice(productCost);
        return productCost;
    }

    @Override
    public void failedPayment(UUID orderId) {

    }


    private Payment getFromRepository(UUID orderId) {
        return paymentRepository.findById(orderId).orElseThrow(() -> new NoOrderFoundException("Order with id" + orderId + " not found "));
    }
}
