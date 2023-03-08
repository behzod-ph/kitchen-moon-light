package uz.isdaha.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavoriteProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User user;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean favourite = false;

    public FavoriteProduct(Product product, User user, boolean favourite) {
        this.product = product;
        this.user = user;
        this.favourite = favourite;
    }
}
