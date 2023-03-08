package uz.isdaha.entity;


import lombok.*;
import org.hibernate.annotations.Where;
import uz.isdaha.entity.BaseEntity;
import uz.isdaha.entity.lang.LocalizedString;
import uz.isdaha.entity.lang.MultilingualString;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Where(clause = "deleted=false")
@ToString
public class Branch extends BaseEntity<String> {


    @OneToOne

    private Address address;
    @Column(length = 30)
    private String target;
    @Column(length = 30)
    private String contacts;

    private int capacity;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BranchesImages> images;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "information")
    private MultilingualString information = new MultilingualString();

    public String getInformation(String lang) {
        return this.information.getText(lang);
    }


    public void setInformation(MultilingualString information) {
        this.information = information;
    }

    public void setInformation(Map<String, LocalizedString> information) {
        MultilingualString multilingualString = new MultilingualString();
        multilingualString.setMap(information);
        this.information = multilingualString;
    }

    public Map<String, LocalizedString> getInformation() {
        return information.getMap();
    }
}
