package prac.pizzashop.spring.api.service.product;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import prac.pizzashop.spring.api.controller.product.request.ProductCreateRequest;
import prac.pizzashop.spring.api.service.product.response.ProductResponse;
import prac.pizzashop.spring.domain.product.Product;
import prac.pizzashop.spring.domain.product.ProductRepository;
import prac.pizzashop.spring.domain.product.ProductSellingStatus;
import prac.pizzashop.spring.domain.product.ProductType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static prac.pizzashop.spring.domain.product.ProductSellingStatus.*;
import static prac.pizzashop.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void createProduct() {
        // Given
        Product product = createProduct("001", PIZZA, SELLING, "불고기 피자", 15_000);
        productRepository.save(product);

        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(PIZZA)
            .sellingStatus(SELLING)
            .name("치즈 피자")
            .price(15_000)
            .build();

        // When
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        // Then
        assertThat(productResponse)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .contains("002", PIZZA, SELLING, "치즈 피자", 15_000);

        List<Product> products = productRepository.findAll();
        assertThat(products)
            .hasSize(2)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .containsExactlyInAnyOrder(
                tuple("001", PIZZA, SELLING, "불고기 피자", 15_000),
                tuple("002", PIZZA, SELLING, "치즈 피자", 15_000)
            );
    }

    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
    @Test
    void createProductWhenProductsIsEmpty() {
        // Given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(PIZZA)
            .sellingStatus(SELLING)
            .name("치즈 피자")
            .price(15_000)
            .build();

        // When
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        // Then
        assertThat(productResponse)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .contains("001", PIZZA, SELLING, "치즈 피자", 15_000);

        List<Product> products = productRepository.findAll();
        assertThat(products)
            .hasSize(1)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .containsExactlyInAnyOrder(
                tuple("001", PIZZA, SELLING, "치즈 피자", 15_000)
            );
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