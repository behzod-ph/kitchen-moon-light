package uz.isdaha.integration.sms;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "sms", url = "${sms.api.url}", configuration = CustomRequestInception.class)
public interface SmsFeign {

    @PostMapping(value = "message/sms/send")
    String sendActivationCode(@RequestBody Map<String, String> map);

}
