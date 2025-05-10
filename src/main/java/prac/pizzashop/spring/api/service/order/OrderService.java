package prac.pizzashop.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prac.pizzashop.spring.api.controller.order.request.OrderCreateRequest;
import prac.pizzashop.spring.api.service.order.response.OrderResponse;
import prac.pizzashop.spring.domain.order.Order;
import prac.pizzashop.spring.domain.order.OrderRepository;
import prac.pizzashop.spring.domain.product.Product;
import prac.pizzashop.spring.domain.product.ProductRepository;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> duplicateProducts = findProductsBy(productNumbers);

        Order order = Order.create(duplicateProducts, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getProductNumber, product -> product));

        return productNumbers.stream()
            .map(productMap::get)
            .toList();
    }

}
