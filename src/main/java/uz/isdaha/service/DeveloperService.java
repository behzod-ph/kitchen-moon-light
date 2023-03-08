package uz.isdaha.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.isdaha.entity.HelpTable;
import uz.isdaha.repository.HelperRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeveloperService {
    private final HelperRepository helper;


    public List<CountIp> getCount() {
        return helper.getCountIp();
    }

    public List<HelpTable> getByIp(String ip){
       return helper.findByIp(ip);
    }


    public static interface CountIp {
        String getIp();

        Integer getCount();
    }

}
