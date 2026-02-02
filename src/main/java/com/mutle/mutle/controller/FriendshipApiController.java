package com.mutle.mutle.controller;

import com.mutle.mutle.dto.*;
import com.mutle.mutle.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipApiController {

    private final FriendshipService friendshipService;

    // 1. 친구 이메일/ID 검색
    @GetMapping("/search")
    public ResponseEntity<FriendSearchResponse> searchFriend(
            @RequestParam Long currentUserId,
            @RequestParam String type,
            @RequestParam String keyword) {
        return ResponseEntity.ok(friendshipService.searchFriend(currentUserId, type, keyword));
    }

    // 2. 친구 신청 보내기
    @PostMapping("/request")
    public ResponseEntity<FriendRequestResponse> sendFriendRequest(
            @RequestParam Long meId,
            @RequestBody FriendRequest request) {
        return ResponseEntity.ok(friendshipService.sendFriendRequest(meId, request.getTargetId()));
    }

    // 3. 친구 신청 취소
    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<FriendRequestCancelResponse> cancelFriendRequest(
            @RequestParam Long meId,
            @PathVariable Long requestId) {
        return ResponseEntity.ok(friendshipService.cancelFriendRequest(meId, requestId));
    }

    // 4. 받은 친구 신청 목록 조회
    @GetMapping("/requests/received")
    public ResponseEntity<List<ReceivedRequestResponse>> getReceivedRequests(
            @RequestParam Long userId) {
        return ResponseEntity.ok(friendshipService.getReceivedRequests(userId));
    }

    // 5. 친구 신청 수락/거절
    @PatchMapping("/requests/{requestId}")
    public ResponseEntity<FriendResponse> respondFriendRequest(
            @RequestParam Long userId,
            @PathVariable Long requestId,
            @RequestBody FriendRespondRequest body) {
        return ResponseEntity.ok(friendshipService.respondFriendRequest(userId, requestId, body));
    }

    // 6. 친구 목록 조회
    @GetMapping
    public ResponseEntity<List<FriendListResponse>> getFriendList(
            @RequestParam Long userId) {
        return ResponseEntity.ok(friendshipService.getFriendList(userId));
    }

    // 7. 친구 삭제
    @DeleteMapping("/{targetId}")
    public ResponseEntity<FriendDeleteResponse> deleteFriend(
            @RequestParam Long userId,
            @PathVariable Long targetId) {
        return ResponseEntity.ok(friendshipService.deleteFriend(userId, targetId));
    }
}