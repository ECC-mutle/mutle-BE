package com.mutle.mutle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class SignupResponseDto {
    private Timestamp createdAt;
}
