package uz.isdaha.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BranchesImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Branch branch;

    private String image;

    public BranchesImages(Branch branch, String image) {
        this.branch = branch;
        this.image = image;
    }
}
