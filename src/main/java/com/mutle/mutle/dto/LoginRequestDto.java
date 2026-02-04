package com.mutle.mutle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message="AUTH_101")
    private String userId;

    @NotBlank(message = "AUTH_101")
    private String password;
}
