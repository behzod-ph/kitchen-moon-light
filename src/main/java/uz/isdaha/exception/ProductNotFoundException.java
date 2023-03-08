package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public class ProductNotFoundException extends MotiException {
    @Override
    public ErrorCode errorCode() {
        return ErrorCode.PRODUCT_NOT_FOUND;
    }
}
