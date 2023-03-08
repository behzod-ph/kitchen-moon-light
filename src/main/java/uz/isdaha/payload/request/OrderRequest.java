package uz.isdaha.payload.request;

import lombok.Data;
import uz.isdaha.entity.Order;
import uz.isdaha.enums.OrderStatus;
import uz.isdaha.enums.OrderType;
import uz.isdaha.enums.PaymentMethod;
import uz.isdaha.payload.Creatable;
import uz.isdaha.payload.Modifiable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;
import java.util.List;


@Data
public class OrderRequest implements Creatable, Modifiable {
    private Long deliveryTime;
    private long addressId;
    private double distance;
    private PaymentMethod paymeType;
    private OrderType orderType;
    private String comment;
    private Long branchId;
    List<ProductCount> products;
}
