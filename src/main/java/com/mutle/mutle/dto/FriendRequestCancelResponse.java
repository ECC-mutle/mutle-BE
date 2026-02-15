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
public class FriendRequestCancelResponse {
    private Long friendRequestId;
    private String targetId;
    private String targetNickname;
    private String friendshipStatus;
    private Timestamp cancelledAt;
}
