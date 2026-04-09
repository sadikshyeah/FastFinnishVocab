package my.project.vocab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import my.project.vocab.domain.User;
import my.project.vocab.domain.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User curruser = userRepository.findByUsername(username);

        if (curruser == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        if (!curruser.isEnabled()) {
            throw new DisabledException("User account is not enabled");
        }

        return new org.springframework.security.core.userdetails.User(
                curruser.getUsername(),
                curruser.getPassword(),
                AuthorityUtils.createAuthorityList(curruser.getRole()));
    }
}
