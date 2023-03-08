package uz.isdaha.integration.payme.controller;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.isdaha.integration.payme.payload.PaycomRequestForm;
import uz.isdaha.integration.payme.service.IPaycomService;

@RequestMapping("api/v1/payme")
@RestController
public class PaymeController {
    @Autowired
    private IPaycomService ipaycomService;

    @PostMapping
    JSONObject post(@RequestBody PaycomRequestForm requestForm,
                    @RequestHeader("Authorization") String authorization) {
        return ipaycomService.payWithPaycom(requestForm, authorization);
    }
}
