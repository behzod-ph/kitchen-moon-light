package uz.isdaha.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import uz.isdaha.entity.Product;


@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse  {


    private Long id;

    private String productName;

    private Long categoryId;

    private String categoryName;

    private String description;

    private double price;

    private String imageUrl;

    private double discount;

    private String readyTime;

    private boolean favourite;


    public static ProductResponse toDto(Product product, boolean favourite) {
        return ProductResponse.builder()
            .id(product.getId())
            .productName(product.getProductName())
            .categoryId(product.getCategory().getId())
            .categoryName(product.getCategory().localized())
            .description(product.getDescription())
            .price(product.getPrice().doubleValue())
            .imageUrl(product.getImageUrl())
            .readyTime(product.getReadyTime())
            .discount(product.getDiscount())
            .favourite(favourite)
            .build();
    }


//    public static ProductResponse ofAuditing(Product product) {
//        ProductResponse response = ofCommon(product);
//
//        response.setCreatedDate(product.getCreatedAt());
//        response.setCreatedBy(product.getCreatedBy());
//        response.setUpdatedDate(product.getUpdatedAt());
//        response.setUpdatedBy(product.getUpdatedBy());
//
//        return response;
//    }
}
