package prac.pizzashop.spring.api.service.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import prac.pizzashop.spring.client.mail.MailSendClient;
import prac.pizzashop.spring.domain.history.mail.MailSendHistory;
import prac.pizzashop.spring.domain.history.mail.MailSendHistoryRepository;
import prac.pizzashop.spring.domain.order.Order;
import prac.pizzashop.spring.domain.order.OrderRepository;
import prac.pizzashop.spring.domain.order.OrderStatus;
import prac.pizzashop.spring.domain.orderproduct.OrderProductRepository;
import prac.pizzashop.spring.domain.product.Product;
import prac.pizzashop.spring.domain.product.ProductRepository;
import prac.pizzashop.spring.domain.product.ProductType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static prac.pizzashop.spring.domain.product.ProductSellingStatus.SELLING;
import static prac.pizzashop.spring.domain.product.ProductType.*;

@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @MockitoBean
    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("결제 완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        // Given
        LocalDateTime now = LocalDateTime.of(2025, 5, 14, 0, 0);

        Product product1 = createProduct(PIZZA, "001", 15_000);
        Product product2 = createProduct(PIZZA, "002", 17_000);
        Product product3 = createProduct(PIZZA, "003", 20_000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2025, 5, 13, 23, 59, 59));
        Order order2 = createPaymentCompletedOrder(products, now);
        Order order3 = createPaymentCompletedOrder(products, LocalDateTime.of(2025, 5, 14, 23, 59, 59));
        Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2025, 5, 15, 0, 0, 0));

        when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
            .thenReturn(true);

        // When
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2025, 5, 14), "test@test.com");

        // Then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
            .extracting("content")
            .contains("총 매출 합계는 104000원입니다.");
    }

    private Order createPaymentCompletedOrder(List<Product> products, LocalDateTime now) {
        Order order = Order.builder()
            .products(products)
            .status(OrderStatus.PAYMENT_COMPLETED)
            .registeredDateTime(now)
            .build();
        return orderRepository.save(order);
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