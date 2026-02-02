package com.vshare.vshare.feature.user.boundary;

import com.vshare.vshare.feature.user.control.UserService;
import com.vshare.vshare.feature.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public void create(@Valid @RequestBody User user) {
        userService.createUser(user);
    }
}
