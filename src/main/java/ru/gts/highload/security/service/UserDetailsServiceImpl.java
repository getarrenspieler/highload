package ru.gts.highload.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.gts.highload.model.UserInfo;
import ru.gts.highload.security.model.UserDetailsImpl;
import ru.gts.highload.service.UserService;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserInfo userInfo = userService.get(username);
        return new UserDetailsImpl(userInfo.getId(), userInfo.getPassword());
    }
}
