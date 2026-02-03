package com.vshare.vshare.feature.user.control;

import com.vshare.vshare.feature.user.entity.User;
import com.vshare.vshare.feature.user.entity.UserPrincipal;
import com.vshare.vshare.feature.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByAccount(username);

        if (user==null) throw new UsernameNotFoundException("User 404");

        return new UserPrincipal(user);
    }
}
