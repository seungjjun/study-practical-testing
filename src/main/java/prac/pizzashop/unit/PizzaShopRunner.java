package prac.pizzashop.unit;

import prac.pizzashop.unit.pizza.BulgogiPizza;
import prac.pizzashop.unit.pizza.CheesePizza;

public class PizzaShopRunner {

    public static void main(String[] args) {
        PizzaShop pizzaShop = new PizzaShop();

        pizzaShop.add(new BulgogiPizza());
        pizzaShop.add(new CheesePizza());

        int totalPrice = pizzaShop.calculateTotalPrice();
        System.out.println("총 주문 가격: " + totalPrice + "원");
    }
}
