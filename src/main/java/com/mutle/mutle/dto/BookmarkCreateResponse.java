package com.mutle.mutle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkCreateResponse {
    private Long bookmarkId;
    private Long bottleId;
    private Timestamp bookmarkExpiresAt;
    private Timestamp bookmarkCreatedAt;
    private Timestamp bookmarkUpdatedAt;
}
