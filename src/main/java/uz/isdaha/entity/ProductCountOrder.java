package uz.isdaha.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductCountOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    @Column(nullable = false)
    int count;

    public ProductCountOrder(Product product, int count) {
        this.product = product;
        this.count = count;
    }
}
