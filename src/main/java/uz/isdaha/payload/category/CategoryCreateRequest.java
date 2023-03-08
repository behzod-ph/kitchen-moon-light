package uz.isdaha.payload.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.entity.lang.LocalizedString;
import uz.isdaha.payload.Creatable;
import uz.isdaha.payload.Modifiable;

import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest implements Creatable, Modifiable {

    private Map<String, LocalizedString> categoryName;
    //private MultipartFile iconImage;
    private Long subcategoryId;

}
