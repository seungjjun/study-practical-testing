package prac.pizzashop.unit;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prac.pizzashop.unit.order.Order;
import prac.pizzashop.unit.pizza.BulgogiPizza;
import prac.pizzashop.unit.pizza.CheesePizza;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PizzaShopTest {

    @Test
    @DisplayName("피자를 1개 주문하면 주문 목록에 추가된다.")
    void add() {
        // Given
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();

        // When
        pizzaShop.add(bulgogiPizza);

        // Then
        assertThat(pizzaShop.getPizzas()).hasSize(1);
        assertThat(pizzaShop.getPizzas().get(0).getName()).isEqualTo("불고기 피자");
    }

    @Test
    @DisplayName("피자를 여러 개 주문하면 모두 주문 목록에 추가된다.")
    void addSeveralPizzas() {
        // Given
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();

        // When
        pizzaShop.add(bulgogiPizza, 2);

        // Then
        assertThat(pizzaShop.getPizzas()).hasSize(2);
        assertThat(pizzaShop.getPizzas().get(0).getName()).isEqualTo("불고기 피자");
        assertThat(pizzaShop.getPizzas().get(1).getName()).isEqualTo("불고기 피자");
    }

    @Test
    @DisplayName("피자 주문 수량이 0개일 때 예외가 발생한다.")
    void addZeroPizzas() {
        // Given
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();

        // When && Then
        assertThatThrownBy(() -> pizzaShop.add(bulgogiPizza, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("피자는 1개 이상 주문해야 합니다.");
    }

    @Test
    @DisplayName("주문 했던 피자를 삭제하면 주문 목록에서 제거된다.")
    void remove() {
        // Given
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();

        // When
        pizzaShop.add(bulgogiPizza);
        assertThat(pizzaShop.getPizzas()).hasSize(1);

        pizzaShop.remove(bulgogiPizza);

        // Then
        assertThat(pizzaShop.getPizzas()).isEmpty();
    }

    @Test
    @DisplayName("주문 목록을 비우면 모든 피자가 제거된다.")
    void clear() {
        // Given
        PizzaShop pizzaShop = new PizzaShop();

        // When
        pizzaShop.add(new BulgogiPizza());
        pizzaShop.add(new CheesePizza());
        assertThat(pizzaShop.getPizzas()).hasSize(2);

        pizzaShop.clear();

        // Then
        assertThat(pizzaShop.getPizzas()).isEmpty();
    }

    @Test
    @DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    void calculateTotalPrice() {
        // Given
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();
        CheesePizza cheesePizza = new CheesePizza();

        pizzaShop.add(bulgogiPizza);
        pizzaShop.add(cheesePizza);

        // When
        int totalPrice = pizzaShop.calculateTotalPrice();

        // Then
        assertThat(totalPrice).isEqualTo(35_000);
    }

    @Test
    @DisplayName("영업 시간에 주문하면 정상적으로 주문이 생성된다.")
    void createOrderWithCurrentTime() {
        // Given
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();

        pizzaShop.add(bulgogiPizza);

        // When
        Order order = pizzaShop.createOrder(LocalDateTime.of(2025, 5, 8, 10, 0));

        // Then
        assertThat(order.getPizzas()).hasSize(1);
        assertThat(order.getPizzas().get(0).getName()).isEqualTo("불고기 피자");
    }

    @Test
    @DisplayName("영업 시간 외에 주문하면 주문 생성에 실패한다.")
    void createOrderOutsideOpenTime() {
        // Given
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();

        pizzaShop.add(bulgogiPizza);

        // When && Then
        assertThatThrownBy(() -> pizzaShop.createOrder(LocalDateTime.of(2025, 5, 8, 9, 0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
    }
}
