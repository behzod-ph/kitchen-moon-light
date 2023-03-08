package uz.isdaha.integration.payme.payload;

import lombok.Data;

@Data
public class AdditionalInfo {

    private Long orderId;

    private Integer orderSum;


    public AdditionalInfo(Long orderId, Integer orderSum) {
        this.orderId = orderId;
        this.orderSum = orderSum;
    }
}
