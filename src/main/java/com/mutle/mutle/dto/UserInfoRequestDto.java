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
    @Pattern(regexp = "^[a-z0-9._]{4,20}$", message = "AUTH_003")
    private String userId;
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "AUTH_004")
    private String nickname;
    @Email(message = "AUTH_006")
    private String email;
    private String profileImage;
}