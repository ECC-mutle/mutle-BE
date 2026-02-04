package com.mutle.mutle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserInfoDto {
    private String sub;
    private String email;
    private String name;
}
