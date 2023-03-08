package uz.isdaha.payload.request;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.entity.lang.LocalizedString;
import uz.isdaha.payload.Creatable;
import uz.isdaha.payload.Modifiable;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ProductRequest implements Creatable, Modifiable {

    private Map<String, LocalizedString> name;

    private Map<String, LocalizedString> description;

    private Double price;

    private long categoryId;

    private String readyTime;

    private double discount;

    private String code;


}
