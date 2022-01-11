package app.aribas.auth.service;


import app.aribas.auth.model.UserBas;
import app.aribas.auth.repo.UserBasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringUserService implements UserDetailsService {
    private final UserBasRepository userBasRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserBas credentials = userBasRepository.findByEmail(email);

        if (credentials == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDetails user = User.withUsername(
                credentials.getEmail()).
                password(credentials.getPassword()).
                authorities("USER").build();
        return user;

    }
}
