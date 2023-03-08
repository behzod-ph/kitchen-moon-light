package uz.isdaha.payload.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isdaha.entity.Category;
import uz.isdaha.entity.lang.LocalizedString;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryAdminDto {
    private Long id;
    private Map<String, LocalizedString> categoryName;
    private Long subcategoryId;
    private String iconUrl;


    public static CategoryAdminDto toDto(Category category) {
        return new CategoryAdminDto(
            category.getId(),
            category.getCategoryName(),
            (category.getParentCategory() != null) ? category.getParentCategory().getId() : null,
            category.getIconUrl()
        );
    }

}
