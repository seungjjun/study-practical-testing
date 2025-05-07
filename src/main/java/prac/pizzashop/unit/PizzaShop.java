package prac.pizzashop.unit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import prac.pizzashop.unit.order.Order;
import prac.pizzashop.unit.pizza.Pizza;

@Getter
public class PizzaShop {

    private final List<Pizza> pizzas = new ArrayList<>();

    public void add(Pizza pizza) {
        pizzas.add(pizza);
    }

    public void remove(Pizza pizza) {
        pizzas.remove(pizza);
    }

    public void clear() {
        pizzas.clear();
    }

    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (Pizza pizza : pizzas) {
            totalPrice += pizza.getPrice();
        }
        return totalPrice;
    }

    public Order createOrder() {
        return new Order(LocalDateTime.now(), pizzas);
    }

}
