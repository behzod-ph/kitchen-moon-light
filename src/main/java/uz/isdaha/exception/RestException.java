package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public class RestException extends MotiException{
    private ErrorCode errorCode;
    @Override
   public ErrorCode errorCode() {
        return errorCode;
    }

    public RestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
