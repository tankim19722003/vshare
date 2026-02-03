package com.vshare.vshare.feature.user.boundary.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserLoginRequest {
    private String account;
    private String password;
}
