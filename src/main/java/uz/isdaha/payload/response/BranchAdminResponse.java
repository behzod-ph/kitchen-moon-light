package uz.isdaha.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.entity.Branch;
import uz.isdaha.entity.BranchesImages;
import uz.isdaha.entity.lang.LocalizedString;
import uz.isdaha.entity.lang.MultilingualString;
import uz.isdaha.payload.Creatable;
import uz.isdaha.payload.Modifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchAdminResponse implements Creatable, Modifiable {

    private long id;
    private long addressId;
    private int capacity;
    private String contacts;
    private List<String> imageUrl;
    private Map<String, LocalizedString> information;
    private String target;

    public static BranchAdminResponse toDto(Branch branch) {
        List<String> imageUrl = new ArrayList<>();
        if (branch.getImages() != null) {
            imageUrl = branch.getImages().stream().map(BranchesImages::getImage).distinct().collect(Collectors.toList());

        }
        return new BranchAdminResponse(
            branch.getId(),
            branch.getAddress().getId(),
            branch.getCapacity(),
            branch.getContacts(),
            imageUrl,
            branch.getInformation(),
            branch.getTarget()
        );
    }
}

