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
public class FriendResponse {
    private Long friendRequestId;
    private Long id;
    private String nickname;
    private String friendshipStatus;
    private Timestamp updatedAt;
}
