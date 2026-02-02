package com.mutle.mutle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReceivedRequestResponse {

    private Long friendRequestId;
    private Long id;
    private String nickname;
    private String profileImage;
    private String bio;
    private RepMusicInfo repMusicInfo;
    private Timestamp requestedAt;

    @Getter @Builder
    public static class RepMusicInfo {
        private String trackName;
        private String artistName;
    }
}
