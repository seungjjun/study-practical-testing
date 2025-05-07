package prac.pizzashop.unit;

import org.junit.jupiter.api.Test;
import prac.pizzashop.unit.pizza.BulgogiPizza;
import prac.pizzashop.unit.pizza.CheesePizza;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PizzaShopTest {

    @Test
    void add() {
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();
        pizzaShop.add(bulgogiPizza);

        assertThat(pizzaShop.getPizzas()).hasSize(1);
        assertThat(pizzaShop.getPizzas().get(0).getName()).isEqualTo("불고기 피자");
    }

    @Test
    void addSeveralPizzas() {
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();

        pizzaShop.add(bulgogiPizza, 2);

        assertThat(pizzaShop.getPizzas()).hasSize(2);
        assertThat(pizzaShop.getPizzas().get(0).getName()).isEqualTo("불고기 피자");
        assertThat(pizzaShop.getPizzas().get(1).getName()).isEqualTo("불고기 피자");
    }

    @Test
    void addZeroPizzas() {
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();

        assertThatThrownBy(() -> pizzaShop.add(bulgogiPizza, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("피자는 1개 이상 주문해야 합니다.");
    }

    @Test
    void remove() {
        PizzaShop pizzaShop = new PizzaShop();
        BulgogiPizza bulgogiPizza = new BulgogiPizza();

        pizzaShop.add(bulgogiPizza);
        assertThat(pizzaShop.getPizzas()).hasSize(1);

        pizzaShop.remove(bulgogiPizza);
        assertThat(pizzaShop.getPizzas()).isEmpty();
    }

    @Test
    void clear() {
        PizzaShop pizzaShop = new PizzaShop();

        pizzaShop.add(new BulgogiPizza());
        pizzaShop.add(new CheesePizza());
        assertThat(pizzaShop.getPizzas()).hasSize(2);

        pizzaShop.clear();
        assertThat(pizzaShop.getPizzas()).isEmpty();
    }
}
