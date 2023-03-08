package uz.isdaha.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import uz.isdaha.enums.OrderStatus;
import uz.isdaha.enums.OrderType;
import uz.isdaha.enums.PaymentMethod;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Order extends BaseEntity<String> {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    private Set<ProductCountOrder> products;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Date deliveryTime;

    @ManyToOne
    private Address address;

    private double distance;

    private BigDecimal totalPrice;

    @Column(columnDefinition = "boolean default false")
    private boolean paid;

    private String transactionID;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;
}
