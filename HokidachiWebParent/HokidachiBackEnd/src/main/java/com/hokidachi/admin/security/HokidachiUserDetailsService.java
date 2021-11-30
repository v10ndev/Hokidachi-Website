package com.hokidachi.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hokidachi.admin.user.UserRepository;
import com.hokidachi.common.entity.User;

public class HokidachiUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(email);
        if(user != null) {
            return new HokidachiUserDetails(user);
        }

        throw new UsernameNotFoundException("Không thể tìm thấy người dùng với email: " + email);
    }

}
