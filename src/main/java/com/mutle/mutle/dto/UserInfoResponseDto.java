package com.mutle.mutle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDto {
    private String userId;
    private String nickName;
    private String email;
    private String profileImage;
}
