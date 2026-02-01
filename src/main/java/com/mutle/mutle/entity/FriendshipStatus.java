package com.mutle.mutle.entity;

public enum FriendshipStatus {
    NONE,             // 아무 관계 없음
    REQUEST_SENT,     // 내가 신청을 보낸 상태
    REQUEST_RECEIVED, // 상대가 내게 신청을 보낸 상태
    ACCEPTED          // 이미 친구인 상태
}
