package uz.isdaha.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeeDistance extends BaseEntity<String> {
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private BigDecimal deliveryPayment;


    public FeeDistance(Long id , BigDecimal price, BigDecimal deliveryPayment) {
        this.setId(id);
        this.price = price;
        this.deliveryPayment = deliveryPayment;
    }
}
