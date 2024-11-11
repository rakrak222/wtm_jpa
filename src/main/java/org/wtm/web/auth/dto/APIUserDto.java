package org.wtm.web.auth.dto;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
@ToString
public class APIUserDto extends User {

    private String email;

    public APIUserDto(String email, String password, Collection<GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.email = email;
    }
}
