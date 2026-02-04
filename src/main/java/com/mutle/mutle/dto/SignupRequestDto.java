package com.mutle.mutle.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto{

    @NotBlank(message="AUTH_003")
    @Pattern(regexp = "^[a-z0-9._]{4,20}$", message = "AUTH_003")
    private String userId; // 4~20자 영문 소문자, 숫자, _, .

    @NotBlank(message="AUTH_004")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "AUTH_004")
    private String nickname;

    @NotBlank(message="AUTH_005")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$", message ="AUTH_005")
    private String password; // 8~16자 영문, 숫자, 특수문자

    @NotBlank(message="AUTH_006")
    @Email(message="AUTH_006")
    private String email;

    private String profileImage; //nullable
}
