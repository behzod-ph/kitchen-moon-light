package uz.isdaha.payload.request;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeeDistanceRequest {
    private BigDecimal price;
    private BigDecimal deliveryPayment;
}
