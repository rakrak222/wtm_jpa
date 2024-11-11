package org.wtm.web.auth.security;


import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wtm.web.auth.dto.APIUserDto;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.user.model.User;

@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> result = userRepository.findByEmail(username);

        log.info(result.get());

        User user = result.orElseThrow(() -> new UsernameNotFoundException("Cannot find username: " + username ));

        APIUserDto dto = new APIUserDto(
            user.getEmail(),
            user.getPassword(),
            user.getAuthorities()
        );
            
        log.info("return: {}", dto);
        // 해당 사용자가 존재할 때 APIUserDto 반환
        return dto;
    }
}
