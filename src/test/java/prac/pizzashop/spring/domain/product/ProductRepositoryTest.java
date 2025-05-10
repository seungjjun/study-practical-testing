package prac.pizzashop.spring.domain.product;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
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
        Product product1 = Product.builder()
            .productNumber("001")
            .type(PIZZA)
            .sellingStatus(SELLING)
            .name("불고기 피자")
            .price(15_000)
            .build();

        Product product2 = Product.builder()
            .productNumber("002")
            .type(DRINK)
            .sellingStatus(HOLD)
            .name("콜라")
            .price(2_000)
            .build();

        Product product3 = Product.builder()
            .productNumber("003")
            .type(SIDE)
            .sellingStatus(STOP_SELLING)
            .name("하와이안 피자")
            .price(20_000)
            .build();

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
        Product product1 = Product.builder()
            .productNumber("001")
            .type(PIZZA)
            .sellingStatus(SELLING)
            .name("불고기 피자")
            .price(15_000)
            .build();

        Product product2 = Product.builder()
            .productNumber("002")
            .type(DRINK)
            .sellingStatus(HOLD)
            .name("콜라")
            .price(2_000)
            .build();

        Product product3 = Product.builder()
            .productNumber("003")
            .type(SIDE)
            .sellingStatus(STOP_SELLING)
            .name("하와이안 피자")
            .price(20_000)
            .build();

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

}
