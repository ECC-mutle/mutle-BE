package com.mutle.mutle.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class BottleCreateRequest {

    private Long questionId;
    private MusicInfo musicInfo;
    private String memo;
    private Boolean isShared;

    public String getMemo() {
        return memo;
    }

    @Getter
    @NoArgsConstructor
    public static class MusicInfo {
        private Long musicId;
        private String trackName;
        private String artistName;
        private String artworkUrl60;
    }
}
