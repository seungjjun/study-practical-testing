package prac.pizzashop.unit;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import prac.pizzashop.unit.order.Order;
import prac.pizzashop.unit.pizza.Pizza;

@Getter
public class PizzaShop {

    private static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 0);
    private static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 0);

    private final List<Pizza> pizzas = new ArrayList<>();

    public void add(Pizza pizza) {
        pizzas.add(pizza);
    }

    public void add(Pizza pizza, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("피자는 1개 이상 주문해야 합니다.");
        }

        for (int i = 0; i < count; i++) {
            pizzas.add(pizza);
        }
    }

    public void remove(Pizza pizza) {
        pizzas.remove(pizza);
    }

    public void clear() {
        pizzas.clear();
    }

    public int calculateTotalPrice() {
        return pizzas.stream()
            .mapToInt(Pizza::getPrice)
            .sum();
    }

    public Order createOrder(LocalDateTime currentDateTime) {
        LocalTime currentTime = currentDateTime.toLocalTime();
        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요.");
        }

        return new Order(LocalDateTime.now(), pizzas);
    }

}
