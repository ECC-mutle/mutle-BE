package com.mutle.mutle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FriendListResponse {
    private Long id;
    private String nickname;
    private String profileImage;
    private String bio;
    private RepMusicInfo repMusicInfo;

    @Getter @Builder
    public static class RepMusicInfo {
        private String trackName;
        private String artistName;
        private String artworkUrl60;
    }
}
