package prac.pizzashop.spring.api.controller.order.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import prac.pizzashop.spring.api.service.order.request.OrderCreateServiceRequest;

@Getter
public class OrderCreateRequest {

    @NotEmpty(message = "상품 번호는 필수입니다.")
    private final List<String> productNumbers;

    @Builder
    private OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }

    public OrderCreateServiceRequest toServiceRequest() {
        return OrderCreateServiceRequest.builder()
            .productNumbers(productNumbers)
            .build();
    }
}
