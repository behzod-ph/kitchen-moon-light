package uz.isdaha.integration.payme.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isdaha.entity.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User client;

    //USHBU TO'LOVDAN QANCHA TISHGANI
    @Column(nullable = false)
    private Double paySum;

    private Timestamp payDate = new Timestamp(System.currentTimeMillis());

    private Long orderTransactionId;

    private String transactionId;

    private Boolean cancelled = false;

    public Payment(User client, Double paySum, Timestamp payDate, Long orderTransactionId, String transactionId) {
        this.client = client;
        this.paySum = paySum;
        this.payDate = payDate;
        this.orderTransactionId = orderTransactionId;
        this.transactionId = transactionId;
    }
}
