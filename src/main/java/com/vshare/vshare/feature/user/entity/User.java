package com.vshare.vshare.feature.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotEmpty(message = "Nick name can not be blank")
    @Column(nullable = false, unique = true)
    private String nickName;

    @NotEmpty(message = "Password can not be blank")
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "Account can not be blank")
    @Column(nullable = false, unique = true)
    private String account;
}
