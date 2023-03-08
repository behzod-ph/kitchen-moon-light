package uz.isdaha.integration.payme.service;

public interface PaymeConstans {
    /** ---------method type payme  -------- */
    String CHECK_PERFORM_TRANSACTION = "CheckPerformTransaction";
    String CREATE_TRANSACTION = "CreateTransaction";
    String PERFORM_TRANSACTION = "PerformTransaction";
    String CANCEL_TRANSACTION = "CancelTransaction";
    String CHECK_TRANSACTION = "CheckTransaction";
    String GET_STATEMENT = "GetStatement";

    String ACCOUNT = "account";
    String ORDER = "order";
    String AMOUNT = "amount";
    String TRANSACTION = "Transaction";
    String ACCOUNT_FIELD_NOT_FOUND = "Account field not found";
    String ORDER_NOT_FOUND = "Order not found";
    String AMOUNT_ERROR_NULL = "Amount error or null";
    String AMOUNT_WRONG = "Wrong amount";
    String ORDER_ALREADY_CANCELLED = "Order already cancelled";
    String ORDER_ALREADY_FINISHED = "Order already finished";
    String UNABLE_TRANSACTION = "Unable to complete operation";
    String TRANSACTION_ORDER_NOT_FOUND = "Order transaction not found";
    String UNABLE_CANCEL_TRANSACTION = "Unable to cancel operation";
    String ORDER_ALREADY_DELIVERED = "Order already delivered";


    Long TIME_EXPIRED_PAYCOM_ORDER = 43_200_000L;

}
