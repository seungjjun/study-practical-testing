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
import prac.pizzashop.spring.domain.stock.Stock;
import prac.pizzashop.spring.domain.stock.StockRepository;
import static org.assertj.core.api.Assertions.*;
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
    private StockRepository stockRepository;

    @Autowired
    private OrderService orderService;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
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

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        // When
        OrderResponse response = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

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

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        // When
        OrderResponse response = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

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

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        // Given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(PIZZA, "001", 15_000);
        Product product2 = createProduct(DRINK, "002", 2_000);
        Product product3 = createProduct(SIDE, "003", 5_000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001", "002", "003"))
            .build();

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        // When
        OrderResponse response = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

        // Then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 37_000);
        assertThat(response.getProducts()).hasSize(4)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 15_000),
                tuple("001", 15_000),
                tuple("002", 2_000),
                tuple("003", 5_000)
            );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
            .extracting("productNumber", "quantity")
            .containsExactlyInAnyOrder(
                tuple("001", 0),
                tuple("002", 1)
            );
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    @Test
    void createOrderWithNoStock() {
        // Given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(PIZZA, "001", 15_000);
        Product product2 = createProduct(DRINK, "002", 2_000);
        Product product3 = createProduct(SIDE, "003", 5_000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001", "002", "003"))
            .build();

        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 1);
        stockRepository.saveAll(List.of(stock1, stock2));

        // When && Then
        assertThatThrownBy(() -> orderService.createOrder(request.toServiceRequest(), registeredDateTime))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("재고가 부족한 상품이 있습니다.");
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
