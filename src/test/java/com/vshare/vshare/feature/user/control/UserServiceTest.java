package com.vshare.vshare.feature.user.control;

import com.vshare.vshare.common.response.ApiResponse;
import com.vshare.vshare.feature.user.boundary.dto.response.UserLoginResponse;
import com.vshare.vshare.feature.user.entity.User;
import com.vshare.vshare.feature.user.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    private User user;

    @BeforeEach
    void init() {
        user = new User("account", "nickName", "$2a$10$T.llbnTzqPvEHQ2KPM33CuOKd2Dx7DjjJeaZB0qTEDujB6toWtQ.G", "abc");
    }

    @Test
    void register_UserExisting_ThrowResponseStatusException() {

//         given
        when(userRepo.existsByAccountOrNickName(user.getAccount(), user.getNickName())).thenReturn(true);

//        action
        ResponseStatusException userExistingException = assertThrows(ResponseStatusException.class,
                () -> userService.create(user));

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
        userService.create(user);

        // assert
        verify(userRepo).existsByAccountOrNickName(user.getAccount(), user.getNickName());
        verify(encoder, times(1)).encode(currentPassword);
        verify(userRepo).save(any());

    }

    @Test
    void login_ValidUser_ReturnToken() {

        //given
        String account = "abc";
        String password = "password";

        // when
        when(userRepo.findByAccount(account)).thenReturn(user);
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        // then
        ApiResponse<UserLoginResponse> response = userService.login(account, password);

        assertNotNull(response);
        verify(userRepo).findByAccount(account);
        assertNotEquals("", response.getData().getToken());


    }

    @Test
    void login_IncorrectPassword_ReturnToken() {

        //given
        String account = "abc";
        String password = "password";

        // when
        when(userRepo.findByAccount(account)).thenReturn(user);
        when(encoder.matches(password, user.getPassword())).thenReturn(false);

        // then
        ResponseStatusException response = assertThrows(ResponseStatusException.class, () -> userService.login(account, password));

        verify(userRepo).findByAccount(account);
        assertEquals("Tài khoản hoặc mật khẩu không đúng", response.getReason());

    }
}
