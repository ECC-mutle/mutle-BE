package com.mutle.mutle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message="DUPLICATE_EMAIL")
    private String userId;

    @NotBlank(message = "INVALID_PASSWORD")
    private String password;
}
