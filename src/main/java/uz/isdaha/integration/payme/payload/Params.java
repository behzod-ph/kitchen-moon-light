package uz.isdaha.integration.payme.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class Params {

    private String id;

    private Account account;

    private BigInteger amount;

    private Long time;

    private Integer reason;

    private Long from;

    private Long to;
}
