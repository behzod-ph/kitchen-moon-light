package uz.isdaha.enums;

public enum ErrorCode {
    ADDRESS_NOT_FOUND(100),
    PRODUCT_NOT_FOUND(101),
    CATEGORY_NOT_FOUND(102),
    BRANCH_NOT_FOUND(103),
    SMS_SERVICE_ERROR(104),
    JWT_EXPIRED(105),
    PAYME_ERROR(106),
    ORDER_NOT_FOUND(107),
    USER_NOT_FOUND(108),
    PHONE_NUMBER_OR_PASSWORD_INCORRECT(109),
    ROLE_NOT_FOUND(110),
    USER_NOT_FOUND_OR_DISABLED(111), USER_ALREADY_REGISTERED(112), MANY_ATTEMPTS(113);
    public final int code;

    ErrorCode(int code) {
        this.code = code;
    }
}
