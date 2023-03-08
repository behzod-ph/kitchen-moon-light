package uz.isdaha.integration.sms.response;

@lombok.Data
public class ResponseLoginSms {
    private Data data;
    private String message;
    private String tokenType;
}
