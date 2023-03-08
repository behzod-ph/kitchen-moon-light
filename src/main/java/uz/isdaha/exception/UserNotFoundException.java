package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public class UserNotFoundException extends MotiException{
    @Override
    public ErrorCode errorCode() {
        return ErrorCode.USER_NOT_FOUND;
    }
}
