package uz.isdaha.config.audit;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import uz.isdaha.config.audit.SpringSecurityAuditAwareImp;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "getUser")
public class JpaAuditConfig {

    @Bean
    public AuditorAware<String> getUser() {
        return new SpringSecurityAuditAwareImp();
    }
}
