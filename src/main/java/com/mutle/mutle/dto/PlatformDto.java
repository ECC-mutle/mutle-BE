package com.mutle.mutle.dto;

import com.mutle.mutle.entity.Platform;
import com.mutle.mutle.entity.PlatformName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlatformDto {
    @NotBlank(message = "ISLAND_301")
    private PlatformName platformName;
    private String platformNickname;
}
