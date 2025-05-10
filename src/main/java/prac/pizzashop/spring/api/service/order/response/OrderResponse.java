package prac.pizzashop.spring.api.service.order.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import prac.pizzashop.spring.api.service.product.response.ProductResponse;
import prac.pizzashop.spring.domain.order.Order;

@Getter
public class OrderResponse {

    private final Long id;
    private final int totalPrice;
    private final LocalDateTime registeredDateTime;
    private final List<ProductResponse> products;

    @Builder
    private OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.products = products;
    }

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
            .id(order.getId())
            .totalPrice(order.getTotalPrice())
            .registeredDateTime(order.getRegisteredDateTime())
            .products(order.getOrderProducts().stream()
                .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                .toList())
            .build();
    }
}
