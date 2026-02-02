package com.vshare.vshare.feature.user.repo;

import com.vshare.vshare.feature.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {

    boolean existsByAccountOrNickName(String account, String nickName);

}
