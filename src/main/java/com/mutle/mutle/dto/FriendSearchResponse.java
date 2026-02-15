package com.mutle.mutle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendSearchResponse {
    private String userId;
    private String nickname;
    private String profileImage;
    private String bio;
    private RepMusicInfo repMusicInfo;
    private String friendshipStatus; // NONE, REQUEST_SENT, REQUEST_RECEIVED, ACCEPTED

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepMusicInfo {
        private String trackName;
        private String artistName;
        private String artworkUrl60;
    }
}
