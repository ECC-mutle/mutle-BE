package com.mutle.mutle.dto;

import com.mutle.mutle.entity.Bottle;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class BottleCreateResponse {

    private Long bottleId;
    private Long questionId;
    private MusicInfo musicInfo;
    private String memo;
    private boolean isShared;
    private Timestamp bottleCreatedAt;
    private Timestamp bottleUpdatedAt;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MusicInfo {
        private Long musicId;
        private String trackName;
        private String artistName;
        private String artworkUrl60;
    }

    // 엔티티 -> DTO 변환 메서드
    public static BottleCreateResponse fromEntity(Bottle bottle) {
        return BottleCreateResponse.builder()
                .bottleId(bottle.getBottleId())
                .questionId(bottle.getTodayQuest().getQuestionId())
                .memo(bottle.getMemo())
                .isShared(bottle.isShared())
                .bottleCreatedAt(bottle.getBottleCreatedAt())
                .musicInfo(MusicInfo.builder()
                        .musicId(bottle.getMusic().getMusicId())
                        .trackName(bottle.getMusic().getTrackName())
                        .artistName(bottle.getMusic().getArtistName())
                        .artworkUrl60(bottle.getMusic().getArtworkUrl60())
                        .build())
                .build();
    }

}
