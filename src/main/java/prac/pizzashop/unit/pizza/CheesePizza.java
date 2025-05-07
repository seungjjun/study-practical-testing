package prac.pizzashop.unit.pizza;

public class CheesePizza implements Pizza {
    @Override
    public String getName() {
        return "치즈 피자";
    }

    @Override
    public int getPrice() {
        return 15000;
    }
}
