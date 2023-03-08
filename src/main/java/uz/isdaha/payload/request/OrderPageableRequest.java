package uz.isdaha.payload.request;

import lombok.Getter;
import lombok.Setter;
import uz.isdaha.enums.OrderType;
import uz.isdaha.enums.PaymentMethod;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderPageableRequest {

    private OrderType orderType;

    private PaymentMethod method;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;

    private String direction;

    private String[] sortBy;
}
