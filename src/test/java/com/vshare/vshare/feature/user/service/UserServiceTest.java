package com.vshare.vshare.feature.user.service;

import com.vshare.vshare.feature.user.control.UserService;
import com.vshare.vshare.feature.user.entity.User;
import com.vshare.vshare.feature.user.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder encoder;

    private User user;

    @BeforeEach
    void init() {
        user = new User("account", "nickName", "password", "abc");
    }

    @Test
    void register_UserExisting_ThrowResponseStatusException() {

//         given
        when(userRepo.existsByAccountOrNickName(user.getAccount(), user.getNickName())).thenReturn(true);

//        action
        ResponseStatusException userExistingException = assertThrows(ResponseStatusException.class,
                () -> userService.createUser(user));

        // assert
        assertEquals("Tài khoản bạn đăng kí đã tồn tại", userExistingException.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, userExistingException.getStatusCode());
        verify(userRepo).existsByAccountOrNickName(user.getAccount(), user.getNickName());
        verify(userRepo, times(0)).save(any());


    }

    @Test
     void register_ValidUser_SaveUserExisting() {

//         given
        String currentPassword = user.getPassword();
        when(userRepo.existsByAccountOrNickName(user.getAccount(), user.getNickName())).thenReturn(false);
        when(encoder.encode(currentPassword)).thenReturn("encodedPassword");

//        action
        userService.createUser(user);

        // assert
        verify(userRepo).existsByAccountOrNickName(user.getAccount(), user.getNickName());
        verify(encoder, times(1)).encode(currentPassword);
        verify(userRepo).save(any());

    }
}
