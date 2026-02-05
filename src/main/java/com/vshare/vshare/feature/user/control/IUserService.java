package com.vshare.vshare.feature.user.control;

import com.vshare.vshare.common.response.ApiResponse;
import com.vshare.vshare.feature.user.boundary.dto.response.UserLoginResponse;
import com.vshare.vshare.feature.user.entity.User;

public interface IUserService {

    void create(User user);

    ApiResponse<UserLoginResponse> login(String account, String password);

    User getUserFromSecurityContext();
}
