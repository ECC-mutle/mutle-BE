package com.mutle.mutle.dto;

import com.mutle.mutle.entity.Bottle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BottleDetailResponse {
    private Long bottleId;
    private Long questionId;
    private String questionText;
    private MusicInfo musicInfo;
    private String memo;
    private SenderInfo sender;
    private int totalCount;
    private boolean isMe;
    private Timestamp bottleCreatedAt;
    private Timestamp bottleUpdatedAt;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MusicInfo {
        private String trackName;
        private String artistName;
        private String artworkUrl60;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SenderInfo {
        private Long senderId;
        private String senderUserId;
        private String senderNickname;
    }

    // 엔티티 -> DTO 변환
    public static BottleDetailResponse fromEntity(Bottle bottle, Long currentUserId) {
        return BottleDetailResponse.builder()
                .bottleId(bottle.getBottleId())
                .questionId(bottle.getTodayQuest().getQuestionId())
                .questionText(bottle.getTodayQuest().getQuestionText())
                .memo(bottle.getMemo())
                .totalCount(bottle.getTotalCount())
                .isMe(bottle.getUser().getId().equals(currentUserId)) // 여기서 isMe 판단!
                .bottleCreatedAt(bottle.getBottleCreatedAt())
                .bottleUpdatedAt(bottle.getBottleUpdatedAt())
                .musicInfo(MusicInfo.builder()
                        .trackName(bottle.getMusic().getTrackName())
                        .artistName(bottle.getMusic().getArtistName())
                        .artworkUrl60(bottle.getMusic().getArtworkUrl60())
                        .build())
                .sender(SenderInfo.builder()
                        .senderId(bottle.getUser().getId())
                        .senderUserId(bottle.getUser().getUserId())
                        .senderNickname(bottle.getUser().getNickname())
                        .build())
                .build();
    }
}
