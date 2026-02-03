package com.vshare.vshare.feature.user.boundary;

import com.vshare.vshare.common.response.ApiResponse;
import com.vshare.vshare.feature.user.boundary.dto.request.UserLoginRequest;
import com.vshare.vshare.feature.user.boundary.dto.response.UserLoginResponse;
import com.vshare.vshare.feature.user.control.UserService;
import com.vshare.vshare.feature.user.entity.User;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PermitAll
    @PostMapping("/register")
    public void create(@Valid @RequestBody User user) {
        log.info("Register user: {}", user);
        userService.create(user);
    }

    @PermitAll
    @PostMapping("login")
    public ApiResponse<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        log.info("login user: {}", userLoginRequest);
        return userService.login(userLoginRequest.getAccount(), userLoginRequest.getPassword());
    }
}
