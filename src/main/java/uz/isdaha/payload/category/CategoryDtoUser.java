package uz.isdaha.payload.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isdaha.entity.Category;



@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDtoUser {
    private Long id;
    private String name;
    private Long parentId;
    private String iconUrl;


    public static CategoryDtoUser toDto(Category category) {
        return new CategoryDtoUser(
            category.getId(),
            category.localized(),
            (category.getParentCategory() != null) ? category.getParentCategory().getId() : null,
            category.getIconUrl()
        );
    }
}
