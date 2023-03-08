package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public abstract class MotiException extends RuntimeException {
   public abstract ErrorCode errorCode();

    public Object response(Object message) {
        return message;
    }
}
