package com.mutle.mutle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class BookmarkListResponse {
    private Long bookmarkId;
    private Long bottleId;
    private Long senderId;
    private String senderNickname;
    private String senderProfileImage;
    private String questionText;
    private MusicInfo musicInfo;
    private Timestamp bookmarkExpiresAt;
    private Timestamp bookmarkCreatedAt;
    private Timestamp bookmarkUpdatedAt;

    @Getter @Builder @AllArgsConstructor
    public static class MusicInfo {
        private String trackName;
        private String artistName;
    }
}
