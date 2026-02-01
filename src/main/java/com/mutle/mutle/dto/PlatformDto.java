package com.mutle.mutle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlatformDto {
    @NotBlank(message = "ISLAND_301")
    private String platformName;
    private String platformNickname;
}
