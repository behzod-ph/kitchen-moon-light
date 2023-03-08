package uz.isdaha.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    String message;
    int statusCode;
    Object data;

    public Response(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }


}
