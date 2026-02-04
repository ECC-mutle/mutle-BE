package com.mutle.mutle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IslandResponseDto {
    private boolean isMe;
    private boolean isFriend;

    private Integer year;
    private Integer month;

    private String nickname;
    private String profileImage;
    private String bio;

    private RepMusicDto repMusic;
    private List<PlatformDto> platforms;
    private List<CalendarDto> calendars;
}
