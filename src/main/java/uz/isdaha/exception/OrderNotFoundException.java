package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public class OrderNotFoundException extends MotiException{
    @Override
    public ErrorCode errorCode() {
        return ErrorCode.ORDER_NOT_FOUND;
    }
}
