package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public class JwtExpiredException extends MotiException{
    @Override
    public ErrorCode errorCode() {
        return ErrorCode.JWT_EXPIRED;
    }
}
