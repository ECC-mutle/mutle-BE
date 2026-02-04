package com.mutle.mutle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RepMusicUpdateRequestDto {
    @NotBlank(message="BLANK_TRACK_NAME")
    private String trackName;
    @NotBlank(message="BLANK_ARTIST_NAME")
    private String artistName;
    private String artworkUrl60;
}
