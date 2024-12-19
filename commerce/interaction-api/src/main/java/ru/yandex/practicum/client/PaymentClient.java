package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

import java.util.UUID;

@FeignClient(name = "payment")
public interface PaymentClient {
    @PostMapping("/api/v1/payment")
    PaymentDto createPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/api/v1/payment/totalCost")
    Double calculateTotalCost(@RequestBody OrderDto orderDto);

    @PostMapping("/api/v1/payment/refund")
    boolean successPayment(@RequestBody UUID orderId);

    @PostMapping("/api/v1/payment/productCost")
    Double calculateProductCost(@RequestBody OrderDto orderDto);

    @PostMapping("/api/v1/payment/failed")
    boolean failedPayment(@RequestBody UUID orderId);
}
