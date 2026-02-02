package com.vshare.vshare.feature.user.control;

import com.vshare.vshare.feature.user.entity.User;
import com.vshare.vshare.feature.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepo userRepo;

    private final BCryptPasswordEncoder encoder;

    @Override
    public void createUser(User user) {
        validateUser(user);
        saveUser(user);
    }

    private void validateUser(User user) {
        if (userRepo.existsByAccountOrNickName(user.getAccount(), user.getNickName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản bạn đăng kí đã tồn tại");
    }

    private void saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

}
