package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public class AddressNotFoundException extends MotiException {

    @Override
    public ErrorCode errorCode() {
        return ErrorCode.ADDRESS_NOT_FOUND;
    }
}
