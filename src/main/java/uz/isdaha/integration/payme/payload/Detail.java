package uz.isdaha.integration.payme.payload;

import lombok.Data;

import java.util.List;

@Data
public class Detail {
    private List<Items> items;
    private Shipping shipping;
}
