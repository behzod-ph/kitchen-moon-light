package uz.isdaha.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPageableRequest {

    private Boolean available;

    private Integer page;

    private Integer size;

    private String direction;

    private String[] sortBy;
}
