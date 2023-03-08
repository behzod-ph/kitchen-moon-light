package uz.isdaha.payload.request;

import uz.isdaha.enums.OrderStatus;
import uz.isdaha.payload.Modifiable;

public class OrderUpdateRequest implements Modifiable {
    public OrderStatus status;
}
