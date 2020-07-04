package de.edu.game.config;

import de.edu.game.model.User;
import de.edu.game.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
/**
 * UserService to get information about the current user
 */
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get the currently
     *
     * @return Optional from {@link User}
     */
    public Optional<User> currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(((UserDetails) auth.getPrincipal()).getUsername());
        if (user.isPresent()) {
            return Optional.of(user.get());
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return userDetails(user.get());
        }
        return null;
    }

    private org.springframework.security.core.userdetails.User userDetails(User user) {
        List privileges = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, privileges);
    }
}
