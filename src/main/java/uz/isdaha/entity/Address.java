package uz.isdaha.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Address extends BaseEntity<String> {
    @Column(length = 30)
    private String district;
    @Column(length = 30)
    private String street;
    @Column(length = 30)
    private String home;
    @Column(length = 30)
    private String entrance;
    @Column(length = 30)
    private String homeNumber;

    // location
    private double longitude;
    private double latitude;

    @ManyToOne
    @JsonIgnore
    private User user;

}
