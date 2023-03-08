package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public class RoleNotFoundException extends MotiException{
    @Override
    public ErrorCode errorCode() {
        return ErrorCode.ROLE_NOT_FOUND;
    }
}
