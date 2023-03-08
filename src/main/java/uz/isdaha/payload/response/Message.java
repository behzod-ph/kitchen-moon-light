package uz.isdaha.payload.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import uz.isdaha.enums.PaymentMethod;

@AllArgsConstructor
@NoArgsConstructor
public class Message {
    public long orderId;
    public String  phoneNumber;
    public PaymentMethod payType;
}
