package prac.pizzashop.spring.domain.product;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    PIZZA("피자"),
    SIDE("사이드"),
    DRINK("음료");

    private final String text;

    public static boolean containsStockType(ProductType type) {
        return List.of(PIZZA, DRINK).contains(type);
    }
}
