package uz.isdaha.payload.response;

import lombok.Builder;
import lombok.Data;
import uz.isdaha.entity.Order;
import uz.isdaha.enums.OrderStatus;
import uz.isdaha.enums.OrderType;
import uz.isdaha.enums.PaymentMethod;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class OrderAdminResponse {

    private long id;
    private Date deliveryTime;
    private PaymentMethod payType;
    private Timestamp creationTime;
    private Timestamp updateTime;
    private String address;
    List<ProductCountResponse> products;
    private BigDecimal price;
    private OrderStatus status;
    private OrderType orderType;
    private String comments;
    private String user;
    private String operator;

    static public OrderAdminResponse toDto(Order order) {
        List<ProductCountResponse> productCountResponseList = new ArrayList<>();
        order.getProducts().stream().distinct().forEach(e -> {
            productCountResponseList.add(new ProductCountResponse(e.getProduct().getProductName(), e.getCount()));
        });
        return OrderAdminResponse.builder()
            .id(order.getId())
            .creationTime(order.getCreatedAt())
            .updateTime(order.getUpdatedAt())
            .address(order.getAddress().getDistrict() + order.getAddress().getStreet())
            .deliveryTime(order.getDeliveryTime())
            .payType(order.getPaymentMethod())
            .products(productCountResponseList)
            .price(order.getTotalPrice())
            .status(order.getStatus())
            .orderType(order.getOrderType())
            .comments(order.getComment())
            .user(order.getUser().getPhoneNumber())
            .operator(order.getUpdatedBy())
            .build();
    }
}
