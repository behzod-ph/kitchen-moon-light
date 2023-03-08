package uz.isdaha.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.isdaha.exception.UserNotFoundException;
import uz.isdaha.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository
            .findByPhoneNumberAndIsActiveTrue(username).
            orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }
}
