package uz.isdaha.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.entity.Address;
import uz.isdaha.entity.Branch;
import uz.isdaha.entity.lang.LocalizedString;
import uz.isdaha.entity.lang.MultilingualString;
import uz.isdaha.payload.Creatable;
import uz.isdaha.payload.Modifiable;

import javax.persistence.EntityManager;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchRequest implements Creatable, Modifiable {

    private Long addressId;
    private int capacity;
    private String contacts;
    private Map<String, LocalizedString> information;
    private String target;

    public Branch toEntity(EntityManager entityManager){
        MultilingualString multilingualString = new MultilingualString();
        multilingualString.setMap(information);
        return Branch.builder()
            .address(entityManager.getReference(Address.class, addressId))
            .capacity(capacity)
            .contacts(contacts)
            .information(multilingualString)
            .target(target)
            .build();
    }


}
