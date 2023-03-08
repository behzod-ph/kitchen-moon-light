package uz.isdaha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import uz.isdaha.entity.HelpTable;
import uz.isdaha.service.DeveloperService;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "api/v1/developer8989")
@ApiIgnore
@RequiredArgsConstructor
public class MyController {

    private final DeveloperService service;

    @GetMapping()
    public List<DeveloperService.CountIp> test() {
        return service.getCount();
    }

    @GetMapping("/{ip}")
    public List<HelpTable> test111(@PathVariable String ip) {
        return service.getByIp(ip);
    }
}
