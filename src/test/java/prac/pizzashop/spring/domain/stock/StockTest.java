package prac.pizzashop.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StockTest {

    @DisplayName("재고 수량이 요청 수량보다 적은지 확인한다.")
    @Test
    void isQuantityLessThan() {
        // Given
        Stock stock = Stock.create("001", 1);
        int quantity = 2;

        // When
        boolean result = stock.isQuantityLessThan(quantity);

        // Then
        assertThat(result).isTrue();
    }

    @DisplayName("재고 수량을 차감한다.")
    @Test
    void deductQuantity() {
        // Given
        Stock stock = Stock.create("001", 5);
        int quantity = 2;

        // When
        stock.deductQuantity(quantity);

        // Then
        assertThat(stock.getQuantity()).isEqualTo(3);
    }

    @DisplayName("재고보다 많은 수의 수량으로 차감 시도 하는 경우 예외가 발생한다.")
    @Test
    void deductQuantity2() {
        // Given
        Stock stock = Stock.create("001", 1);
        int quantity = 2;

        // When && Then
        assertThatThrownBy(() -> stock.deductQuantity(quantity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("차감할 재고 수량이 없습니다.");
    }
}
