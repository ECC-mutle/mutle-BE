package com.mutle.mutle.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequestDto {
    @Pattern(regexp = "^[a-z0-9._]{4,20}$", message = "INVALID_USER_ID")
    private String userId;
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "INVALID_NICKNAME")
    private String nickname;
    @Email(message = "INVALID_EMAIL")
    private String email;
    private String profileImage;
}