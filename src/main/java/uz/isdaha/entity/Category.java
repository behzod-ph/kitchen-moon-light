package uz.isdaha.entity;


import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.context.i18n.LocaleContextHolder;
import uz.isdaha.entity.lang.LocalizedString;
import uz.isdaha.entity.lang.MultilingualString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;


@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Category extends BaseEntity<String>
    implements Serializable {
    private static final long serialVersionUID = 1L;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_name", nullable = false)
    private MultilingualString categoryName = new MultilingualString();

    @ManyToOne(cascade = CascadeType.MERGE)
    private Category parentCategory;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    private String iconUrl;

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }


    public Map<String, LocalizedString> getCategoryName() {
        return categoryName.getMap();
    }

    public void setCategoryName(MultilingualString categoryName) {
        this.categoryName = categoryName;
    }

    public String localized() {
        return categoryName.getText(LocaleContextHolder.getLocale().getLanguage());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

