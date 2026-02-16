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
public class BottleRandomResponse {
    private Long bottleId;
    private Long questionId;
    private String questionText; // 질문 내용 추가!
    private BottleCreateResponse.MusicInfo musicInfo;
    private String memo;
    private SenderInfo sender;   // 보낸 사람 정보 추가!
    private int totalCount;
    private boolean isMe;        // 내가 보낸 건지 여부
    private Timestamp bottleCreatedAt;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SenderInfo {
        private Long senderId;
        private String senderNickname;
    }

    // 엔티티 -> DTO 변환
    public static BottleRandomResponse fromEntity(Bottle bottle, Long currentUserId) {
        return BottleRandomResponse.builder()
                .bottleId(bottle.getBottleId())
                .questionId(bottle.getTodayQuest().getQuestionId())
                .questionText(bottle.getTodayQuest().getQuestionText())
                .memo(bottle.getMemo())
                .isMe(bottle.getUser().getId().equals(currentUserId))
                .sender(SenderInfo.builder()
                        .senderId(bottle.getUser().getId())
                        .senderNickname(bottle.getUser().getNickname())
                        .build())
                .musicInfo(BottleCreateResponse.MusicInfo.builder()
                        .musicId(bottle.getMusic().getMusicId())
                        .trackName(bottle.getMusic().getTrackName())
                        .artistName(bottle.getMusic().getArtistName())
                        .artworkUrl60(bottle.getMusic().getArtworkUrl60())
                        .build())
                .bottleCreatedAt(bottle.getBottleCreatedAt())
                .totalCount(bottle.getTotalCount())
                .build();
    }
}
