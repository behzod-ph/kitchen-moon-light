package uz.isdaha.constan;

import org.springframework.context.i18n.LocaleContextHolder;

public interface RestApiConstants {

    String BASE_API_PATH = "/api/v1";

    String TEMPLATE_PATH = "./src/main/resources/reports/template.xlsx";
    String QR_CODE_API = "https://maps.google.com/?q=";
    String TEMPLATE_SEND_ACTIVATION = "Moti Panasian uchun tasdiqlash kodi :";
    String PHONE_NUMBER_REGEX = "^[998][0-9]{9,15}$";

    String[] OPEN_PAGES_FOR_ALL_METHOD = {
//        "/api/v1/payme",
        "/api/v1/developer8989/**",
        "/api/v1/auth/**",
        "/api/v1/swagger-ui/**",
        "/api/v1/webjars/**",
        "/api/v1/swagger-resources/**",
        "/api/v1/doc/**"
    };

    String[] OPEN_PAGES_FOR_ALL_GET = {
        "/api/v1/category/**",
        "/api/v1/product/**",
        "/api/v1/address/**",
        "/api/v1/branch/**",
    };

    int INVALID = 3006;

}
