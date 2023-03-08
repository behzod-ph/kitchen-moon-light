package uz.isdaha.payload.response;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContextHolder;
import uz.isdaha.entity.Product;
import uz.isdaha.entity.lang.LocalizedString;

import java.util.Map;

@Getter
@Setter
public class ProductAdminResponse {
    private Long id;

    private Map<String, LocalizedString> productName;

    private String category;

    private Map<String, LocalizedString> description;

    private double price;

    private String imageUrl;

    private double discount;

    private String readyTime;

    private boolean isAvailable;

    public static ProductAdminResponse toDTO(Product product) {
        final String lang = LocaleContextHolder.getLocale().getLanguage();
        ProductAdminResponse response = new ProductAdminResponse();
        response.setId(product.getId());
        response.setProductName(product.getProductNameAdmin());
        response.setDescription(product.getDescriptioneAdmin());
        response.setCategory(product.getCategory().localized());
        response.setAvailable(product.isAvailable());
        response.setPrice(product.getPrice().doubleValue());
        response.setImageUrl(product.getImageUrl());
        response.setDiscount(product.getDiscount());
        response.setReadyTime(product.getReadyTime());
        return response;
    }

}
