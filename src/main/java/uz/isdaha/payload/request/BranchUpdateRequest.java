package uz.isdaha.payload.request;

import jdk.dynalink.linker.LinkerServices;
import lombok.Data;
import uz.isdaha.entity.Address;
import uz.isdaha.entity.Branch;
import uz.isdaha.entity.lang.LocalizedString;
import uz.isdaha.entity.lang.MultilingualString;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Data
public class BranchUpdateRequest {

    private Long addressId;
    private int capacity;
    private String contacts;
    private List<String> deletedImages;
    private Map<String, LocalizedString> information;
    private String target;

    public Branch toEntity(EntityManager entityManager) {
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
