package uz.isdaha.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.isdaha.enums.RoleEnum;

import javax.persistence.*;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , unique = true)
    RoleEnum role;


    public Role(RoleEnum role) {
        this.role = role;
    }
}
