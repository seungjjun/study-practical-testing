package prac.pizzashop.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import prac.pizzashop.spring.api.controller.order.request.OrderCreateRequest;
import prac.pizzashop.spring.api.service.order.response.OrderResponse;
import prac.pizzashop.spring.domain.order.OrderRepository;
import prac.pizzashop.spring.domain.orderproduct.OrderProductRepository;
import prac.pizzashop.spring.domain.product.Product;
import prac.pizzashop.spring.domain.product.ProductRepository;
import prac.pizzashop.spring.domain.product.ProductType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static prac.pizzashop.spring.domain.product.ProductSellingStatus.*;
import static prac.pizzashop.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderService orderService;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // Given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(PIZZA, "001", 15_000);
        Product product2 = createProduct(DRINK, "002", 2_000);
        Product product3 = createProduct(SIDE, "003", 5_000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "002"))
            .build();

        // When
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // Then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 17_000);
        assertThat(response.getProducts()).hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 15_000),
                tuple("002", 2_000)
            );
    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        // Given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(PIZZA, "001", 15_000);
        Product product2 = createProduct(DRINK, "002", 2_000);
        Product product3 = createProduct(SIDE, "003", 5_000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001"))
            .build();

        // When
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // Then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 30_000);
        assertThat(response.getProducts()).hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 15_000),
                tuple("001", 15_000)
            );
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
            .type(type)
            .productNumber(productNumber)
            .price(price)
            .sellingStatus(SELLING)
            .name("메뉴 이름")
            .build();
    }
}
