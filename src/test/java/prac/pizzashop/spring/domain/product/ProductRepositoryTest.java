package prac.pizzashop.spring.domain.product;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static prac.pizzashop.spring.domain.product.ProductSellingStatus.*;
import static prac.pizzashop.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
//@SpringBootTest
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매 상태를 가진 상품을 모두 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // Given
        Product product1 = createProduct("001", PIZZA, SELLING, "불고기 피자", 15_000);
        Product product2 = createProduct("002", DRINK, HOLD, "콜라", 2_000);
        Product product3 = createProduct("003", SIDE, STOP_SELLING, "하와이안 피자", 20_000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // When
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // Then
        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "불고기 피자", SELLING),
                tuple("002", "콜라", HOLD)
            );
    }

    @DisplayName("상품 번호 리스트를 받아 상품을 모두 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // Given
        Product product1 = createProduct("001", PIZZA, SELLING, "불고기 피자", 15_000);
        Product product2 = createProduct("002", DRINK, HOLD, "콜라", 2_000);
        Product product3 = createProduct("003", SIDE, STOP_SELLING, "하와이안 피자", 20_000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // When
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // Then
        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "불고기 피자", SELLING),
                tuple("002", "콜라", HOLD)
            );
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    @Test
    void findLatestProduct() {
        // Given
        String targetProductNumber = "003";

        Product product1 = createProduct("001", PIZZA, SELLING, "불고기 피자", 15_000);
        Product product2 = createProduct("002", DRINK, HOLD, "콜라", 2_000);
        Product product3 = createProduct(targetProductNumber, SIDE, STOP_SELLING, "하와이안 피자", 20_000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // When
        String latestProductNumber = productRepository.findLatestProduct();

        // Then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {
        // When
        String latestProductNumber = productRepository.findLatestProduct();

        // Then
        assertThat(latestProductNumber).isNull();
    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus status, String name, int price) {
        return Product.builder()
            .productNumber(productNumber)
            .type(type)
            .sellingStatus(status)
            .name(name)
            .price(price)
            .build();
    }

}
