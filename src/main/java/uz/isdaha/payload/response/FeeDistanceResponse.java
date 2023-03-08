package uz.isdaha.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import uz.isdaha.entity.FeeDistance;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeeDistanceResponse {
    private Long id;
    private BigDecimal price;
    private BigDecimal deliveryPayment;


    public static FeeDistanceResponse toDto(FeeDistance feeDistance) {
        return new FeeDistanceResponse(feeDistance.getId(), feeDistance.getPrice(), feeDistance.getDeliveryPayment());
    }
}
