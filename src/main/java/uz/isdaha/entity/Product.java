package uz.isdaha.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.context.i18n.LocaleContextHolder;
import uz.isdaha.entity.lang.LocalizedString;
import uz.isdaha.entity.lang.MultilingualString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Product extends BaseEntity<String> {
    private double discount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_name")
    private MultilingualString productName = new MultilingualString();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "description")
    private MultilingualString description = new MultilingualString();

    private BigDecimal price;

    private String imageUrl;

    @JsonIgnore
    @ManyToOne
    private Category category;

    private String readyTime;

    private boolean available;

    private String code;


    public String getDescription() {
        return this.description.getText(LocaleContextHolder.getLocale().getLanguage());
    }

    public void setDescription(MultilingualString description) {
        this.description = description;
    }

    public String getProductName() {
        return this.productName.getText(LocaleContextHolder.getLocale().getLanguage());
    }


    public Map<String, LocalizedString> getProductNameAdmin() {
        return this.productName.getMap();
    }

    public Map<String, LocalizedString> getDescriptioneAdmin() {
        return this.description.getMap();
    }

    public void setProductName(MultilingualString name) {
        this.productName = name;

    }

}
