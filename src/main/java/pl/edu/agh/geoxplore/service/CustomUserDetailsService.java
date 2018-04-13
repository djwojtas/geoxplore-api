package pl.edu.agh.geoxplore.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.model.ApplicationUser;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ApplicationUser applicationUser = loadApplicationUserByUsername(s);
        //return new User(applicationUser.getUsername(), applicationUser.getPassword(),
       //         AuthorityUtils.createAuthorityList("ROLE_USER"));

        return User.withDefaultPasswordEncoder().username(applicationUser.getUsername()).password(applicationUser.getPassword()).roles("USER").build();
    }

    public ApplicationUser loadApplicationUserByUsername(String username) {
        return new ApplicationUser("batman", "pass");
    }
}
