package uz.isdaha.integration.sms;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uz.isdaha.exception.SmsTokenCanNotException;
import uz.isdaha.integration.sms.response.ResponseLoginSms;

import java.util.Objects;

@RequiredArgsConstructor
public class CustomRequestInception implements RequestInterceptor {

    private String token;

    @Value("${sms.api.email}")
    private String email;

    @Value("${sms.api.password}")
    private String password;


    @Value("${sms.api.url}")
    private String url;


    private Long expTimestamp = 0L;

    private final RestTemplate restTemplate;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Accept", MediaType.APPLICATION_FORM_URLENCODED.toString());
        template.header("Authorization", "Bearer " + accessToken());
    }

    private String accessToken() {
        return (isExpired() || token == null) ? requestToken() : this.token;
    }


    private String requestToken() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> http = new HttpEntity<>(map, headers);
        ResponseEntity<ResponseLoginSms> response = restTemplate.postForEntity(url + "auth/login", http, ResponseLoginSms.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new  SmsTokenCanNotException();
        }
        this.expTimestamp = System.currentTimeMillis() + 30L * 1000 * 60 * 60 * 24;
        this.token = Objects.requireNonNull(response.getBody()).getData().getToken();
        return this.token;
    }

    private boolean isExpired() {
        return expTimestamp - 2000 <= System.currentTimeMillis();
    }

}
