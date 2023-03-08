package uz.isdaha.integration.payme.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Items {
    private String packageCode;
    private String code;
    private Integer price;
    private Integer vatPercent;
    private Integer count;
    private String title;
}
