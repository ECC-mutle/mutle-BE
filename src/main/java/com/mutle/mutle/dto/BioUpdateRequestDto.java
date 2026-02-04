package com.mutle.mutle.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BioUpdateRequestDto {
    @Size(max = 50, message = "ISLAND_101")
    private String bio;
}
