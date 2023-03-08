package uz.isdaha.integration.payme.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaycomRequestForm {

    private String id;

    private String method;

    private Params params;
}
