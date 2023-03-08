package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public class CategoryNotFoundException extends MotiException{
    @Override
    public ErrorCode errorCode() {
        return ErrorCode.CATEGORY_NOT_FOUND;
    }
}
