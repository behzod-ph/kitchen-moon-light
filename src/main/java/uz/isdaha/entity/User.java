package uz.isdaha.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bouncycastle.cert.dane.DANEEntry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.isdaha.enums.DeviceType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class User extends BaseEntity<String> implements Serializable, UserDetails {

    // for admin
    @Column
    @JsonIgnore
    String password;

    @Column(nullable = false, name = "first_name")
    String firstName;


    @Column(nullable = false, unique = true)
    String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceType device;

    private String deviceToken;

    @Column(columnDefinition = "boolean default false")
    boolean isActive = false;


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role
            .stream()
            .map((rol) -> new SimpleGrantedAuthority("ROLE_" + rol.getRole().name()))
            .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public User(String password, String firstName, String phoneNumber) {
        this.password = password;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
    }
}
