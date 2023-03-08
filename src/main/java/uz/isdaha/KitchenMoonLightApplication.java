package uz.isdaha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication

@EnableJpaRepositories
@EnableSwagger2
@EnableFeignClients
public class KitchenMoonLightApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchenMoonLightApplication.class, args);

    }


}



