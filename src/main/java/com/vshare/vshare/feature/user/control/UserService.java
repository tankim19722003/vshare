package com.vshare.vshare.feature.user.control;

import com.vshare.vshare.common.response.ApiResponse;
import com.vshare.vshare.feature.user.boundary.dto.response.UserLoginResponse;
import com.vshare.vshare.feature.user.entity.User;
import com.vshare.vshare.feature.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepo userRepo;

    private final BCryptPasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public void create(User user) {
        validateRegisteredUser(user);
        saveUser(user);
    }

    private void validateRegisteredUser(User user) {
        if (userRepo.existsByAccountOrNickName(user.getAccount(), user.getNickName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản bạn đăng kí đã tồn tại");
    }

    private void saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public ApiResponse<UserLoginResponse> login(String account, String password) {
        return authenticationUser(validateUser(account, password));
    }

    private Map<String, Object> validateUser(String account, String password) {
        User user = userRepo.findByAccount(account);
        if (user == null || !encoder.matches(password, user.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản hoặc mật khẩu không đúng");

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("rawPassword", password);

        return result;
    }

    private ApiResponse<UserLoginResponse> authenticationUser(Map<String, Object> authenticatedUser) {
        User user = (User) authenticatedUser.get("user");
        String rawPassword = (String) authenticatedUser.get("rawPassword");

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getAccount(), rawPassword));

        if (authentication.isAuthenticated()) return buildUserLoginResponse(user);

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Xác thực người dùng thất bại vui long thử lại");

    }

    private ApiResponse<UserLoginResponse> buildUserLoginResponse(User user) {

        // build userLogin response
        UserLoginResponse userLoginResponse = user.toUserLoginResponse();
        userLoginResponse.setToken(jwtService.generateToken(user.getAccount()));

        ApiResponse<UserLoginResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userLoginResponse);
        apiResponse.setMessage("Đăng nhập thành công");
        apiResponse.setStatus(HttpStatus.OK.value());

        return apiResponse;

    }
}
