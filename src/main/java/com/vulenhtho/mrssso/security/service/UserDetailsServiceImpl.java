package com.vulenhtho.mrssso.security.service;

import com.vulenhtho.mrssso.entity.User;
import com.vulenhtho.mrssso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s)
            throws UsernameNotFoundException {
        User user = userRepository.findByUserName(s)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with -> username or email : " + s)
                );

        return UserPrinciple.build(user);
    }

}
