package uz.isdaha.config.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.isdaha.entity.User;

import java.util.Optional;

public class SpringSecurityAuditAwareImp implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication == null
            || !authentication.isAuthenticated()
            || "anonymousUser".equals("" + authentication.getPrincipal()))) {
            User principal = (User) authentication.getPrincipal();
            return Optional.of(principal.getPhoneNumber());

        }
        return Optional.of("New User");
    }
}
