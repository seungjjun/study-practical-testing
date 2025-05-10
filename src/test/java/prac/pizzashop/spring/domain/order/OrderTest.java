package prac.pizzashop.spring.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prac.pizzashop.spring.domain.product.Product;
import static org.assertj.core.api.Assertions.assertThat;
import static prac.pizzashop.spring.domain.order.OrderStatus.INIT;
import static prac.pizzashop.spring.domain.product.ProductSellingStatus.SELLING;
import static prac.pizzashop.spring.domain.product.ProductType.PIZZA;

class OrderTest {

    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // Given
        List<Product> products = List.of(
            createProduct("001", 15_000),
            createProduct("002", 2_000)
        );

        // When
        Order order = Order.create(products, LocalDateTime.now());

        // Then
        assertThat(order.getTotalPrice()).isEqualTo(17_000);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
    @Test
    void init() {
        // Given
        List<Product> products = List.of(
            createProduct("001", 15_000),
            createProduct("002", 2_000)
        );

        // When
        Order order = Order.create(products, LocalDateTime.now());

        // Then
        assertThat(order.getStatus()).isEqualByComparingTo(INIT);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
    @Test
    void registeredDateTime() {
        // Given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        List<Product> products = List.of(
            createProduct("001", 15_000),
            createProduct("002", 2_000)
        );

        // When
        Order order = Order.create(products, registeredDateTime);

        // Then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
            .type(PIZZA)
            .productNumber(productNumber)
            .price(price)
            .sellingStatus(SELLING)
            .name("메뉴 이름")
            .build();
    }

}
