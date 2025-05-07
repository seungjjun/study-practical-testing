package prac.pizzashop.unit.pizza;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class BulgogiPizzaTest {

    @Test
    void getName() {
        Pizza pizza = new BulgogiPizza();

        assertThat(pizza.getName()).isEqualTo("불고기 피자");
    }

    @Test
    void getPrice() {
        Pizza pizza = new BulgogiPizza();

        assertThat(pizza.getPrice()).isEqualTo(20000);
    }

}
