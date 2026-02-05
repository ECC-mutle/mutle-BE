package com.mutle.mutle.dto;

import com.mutle.mutle.entity.PlatformName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlatformDto {
    @NotNull(message = "BLANK_PLATFORM_NAME")
    private PlatformName platformName;
    private String platformNickname;
}
