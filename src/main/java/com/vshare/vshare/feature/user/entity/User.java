package com.vshare.vshare.feature.user.entity;

import com.vshare.vshare.feature.user.boundary.dto.response.UserLoginResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotEmpty(message = "Nick name can not be blank")
    @Column(name = "nick_name",nullable = false, unique = true)
    private String nickName;

    @NotEmpty(message = "Password can not be blank")
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "Account can not be blank")
    @Column(nullable = false, unique = true)
    private String account;

    public UserLoginResponse toUserLoginResponse() {
        return UserLoginResponse.builder()
                .id(id)
                .account(account)
                .nickName(nickName)
                .build();
    }
}
