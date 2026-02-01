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
public class FriendRequestResponse {
    private Long friendRequestId;
    private String targetNickname;
    private String friendshipStatus;
    private Timestamp friendshipCreatedAt;
}
