package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public class SmsTokenCanNotException extends MotiException{
    @Override
    public ErrorCode errorCode() {
        return ErrorCode.SMS_SERVICE_ERROR;
    }
}
