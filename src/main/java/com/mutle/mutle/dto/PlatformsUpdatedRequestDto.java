package com.mutle.mutle.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlatformsUpdatedRequestDto {
    @Valid
    private List<PlatformDto> platforms;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlatformDto {
        @NotBlank(message = "ISLAND_301")
        private String platformName;

        private String platformNickname;
    }
}
