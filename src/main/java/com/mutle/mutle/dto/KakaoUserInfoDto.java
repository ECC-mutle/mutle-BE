package com.mutle.mutle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfoDto {
    private String userId;
    private String email;
    private String nickname;
}
