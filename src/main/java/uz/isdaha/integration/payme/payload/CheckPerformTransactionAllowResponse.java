package uz.isdaha.integration.payme.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckPerformTransactionAllowResponse {

    //В объекте additional биллинг поставщика может возвращать дополнительную информацию
    // (баланс пользователя, данные о заказе).
    // Кроме того, добавляя объект additional, следует сообщить об этом техническому специалисту Payme Business.
    private AdditionalInfo additional;

    private Boolean allow = false;

    private Detail detail;

    public CheckPerformTransactionAllowResponse(AdditionalInfo additional, Boolean allow) {
        this.additional = additional;
        this.allow = allow;
    }
}
