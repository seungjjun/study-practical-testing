package prac.pizzashop.unit.pizza;

public class BulgogiPizza implements Pizza {
    @Override
    public String getName() {
        return "불고기 피자";
    }

    @Override
    public int getPrice() {
        return 20000;
    }
}
