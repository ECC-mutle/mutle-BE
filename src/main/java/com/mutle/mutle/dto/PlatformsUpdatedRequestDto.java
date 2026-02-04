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

}
