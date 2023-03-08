package uz.isdaha.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.entity.Branch;
import uz.isdaha.entity.BranchesImages;
import uz.isdaha.entity.lang.LocalizedString;
import uz.isdaha.payload.Creatable;
import uz.isdaha.payload.Modifiable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchUserResponse  implements Creatable, Modifiable {

    private long id;
    private long address;
    private int capacity;
    private String contacts;
    private List<String > imageUrl;
    private String information;
    private String target;

    public static BranchUserResponse toDto(Branch branch) {
        final String lang = LocaleContextHolder.getLocale().getLanguage();
        return new BranchUserResponse(
            branch.getId(),
            branch.getAddress().getId(),
            branch.getCapacity(),
            branch.getContacts(),
            branch.getImages().stream().map(BranchesImages::getImage).distinct().collect(Collectors.toList()),
            branch.getInformation(lang),
            branch.getTarget()
        );
    }
}
