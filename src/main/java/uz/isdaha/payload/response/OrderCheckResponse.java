package uz.isdaha.payload.response;

import lombok.Builder;
import lombok.Data;
import uz.isdaha.entity.Order;
import uz.isdaha.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class OrderCheckResponse {
    private long id;
    private String phoneNumber;
    private Date deliveryTime;
    private BigDecimal price;
    private PaymentMethod payType;
    private List<ProductCountResponse> products;
    private boolean paid;
    private byte[] addressQR;

    static public OrderCheckResponse toDto(Order order, byte[] addressQR) {
        List<ProductCountResponse> productCountResponseList = new ArrayList<>();
        System.out.println(order.getProducts().size());
        order.getProducts().forEach(e -> {
            productCountResponseList.add(new ProductCountResponse(e.getProduct().getProductName(), e.getCount()));
        });
        return OrderCheckResponse.builder()
            .id(order.getId())
            .phoneNumber(order.getUser().getPhoneNumber())
            .deliveryTime(order.getDeliveryTime())
            .payType(order.getPaymentMethod())
            .products(productCountResponseList)
            .price(order.getTotalPrice())
            .paid(order.isPaid())
            .addressQR(addressQR)
            .build();
    }
}

