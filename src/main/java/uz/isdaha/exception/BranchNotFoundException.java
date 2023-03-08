package uz.isdaha.exception;

import uz.isdaha.enums.ErrorCode;

public class BranchNotFoundException extends MotiException{
    @Override
    public ErrorCode errorCode() {
        return ErrorCode.BRANCH_NOT_FOUND;
    }
}
