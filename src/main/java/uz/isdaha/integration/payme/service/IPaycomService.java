package uz.isdaha.integration.payme.service;


import net.minidev.json.JSONObject;
import uz.isdaha.integration.payme.payload.PaycomRequestForm;

public interface IPaycomService {

    JSONObject payWithPaycom(PaycomRequestForm requestForm, String auth);


}
