package uz.isdaha.exception;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import uz.isdaha.enums.ErrorCode;

public class PaymeException extends MotiException {


    public PaymeException(JSONRPC2Response response) {
        super();
    }

    @Override
    public ErrorCode errorCode() {
        return ErrorCode.PAYME_ERROR;
    }


}
