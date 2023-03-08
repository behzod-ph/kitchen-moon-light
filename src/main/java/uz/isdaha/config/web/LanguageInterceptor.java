package uz.isdaha.config.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uz.isdaha.util.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;


@Component
public class LanguageInterceptor implements HandlerInterceptor {


    @Autowired
     private RequestService service;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        service.getClientIp(request, response) ;

        String lang = request.getHeader("hl");
        lang = (lang != null) ? lang : "uz";
        LocaleContextHolder.setLocale(Locale.forLanguageTag(lang));
        return true;
    }

}
