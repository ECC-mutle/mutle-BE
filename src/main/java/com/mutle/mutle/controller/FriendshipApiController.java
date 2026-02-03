package com.mutle.mutle.controller;

import com.mutle.mutle.dto.*;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.jwt.JwtUtil;
import com.mutle.mutle.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipApiController {

    private final FriendshipService friendshipService;
    private final JwtUtil jwtUtil;

    private Long getUserIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }
        return jwtUtil.getId(token.substring(7));
    }

    // 친구 이메일/ID 검색
    @GetMapping("/search")
    public ApiResponse<FriendSearchResponse> searchFriend(
            @RequestHeader("Authorization") String token,
            @RequestParam String type,
            @RequestParam String keyword) {
        Long id = getUserIdFromToken(token);
        FriendSearchResponse data = friendshipService.searchFriend(id, type, keyword);
        return ApiResponse.success("사용자 검색 성공", data);
    }

    // 친구 신청 보내기
    @PostMapping("/request")
    public ApiResponse<FriendRequestResponse> sendFriendRequest(
            @RequestHeader("Authorization") String token,
            @RequestBody FriendRequest request) {
        Long id = getUserIdFromToken(token);
        FriendRequestResponse data = friendshipService.sendFriendRequest(id, request.getTargetId());
        return ApiResponse.success("친구 신청이 완료되었습니다.", data);
    }

    // 친구 신청 취소
    @DeleteMapping("/requests/{requestId}")
    public ApiResponse<FriendRequestCancelResponse> cancelFriendRequest(
            @RequestHeader("Authorization") String token,
            @PathVariable Long requestId) {
        Long id = getUserIdFromToken(token);
        FriendRequestCancelResponse data = friendshipService.cancelFriendRequest(id, requestId);
        return ApiResponse.success("친구 신청 취소 완료", data);
    }

    // 받은 친구 신청 목록 조회
    @GetMapping("/requests/received")
    public ApiResponse<List<ReceivedRequestResponse>> getReceivedRequests(
            @RequestHeader("Authorization") String token) {
        Long id = getUserIdFromToken(token);
        List<ReceivedRequestResponse> data = friendshipService.getReceivedRequests(id);
        return ApiResponse.success("받은 신청 목록 조회 성공", data);
    }

    // 친구 신청 수락/거절
    @PatchMapping("/requests/{requestId}")
    public ApiResponse<FriendResponse> respondFriendRequest(
            @RequestHeader("Authorization") String token,
            @PathVariable Long requestId,
            @RequestBody FriendRespondRequest body) {
        Long id = getUserIdFromToken(token);
        FriendResponse data = friendshipService.respondFriendRequest(id, requestId, body);
        return ApiResponse.success("신청 처리가 완료되었습니다.", data);
    }

    // 친구 목록 조회
    @GetMapping
    public ApiResponse<List<FriendListResponse>> getFriendList(
            @RequestHeader("Authorization") String token) {
        Long id = getUserIdFromToken(token);
        List<FriendListResponse> data = friendshipService.getFriendList(id);
        return ApiResponse.success("친구 목록 조회 성공", data);
    }

    // 친구 삭제
    @DeleteMapping("/{targetId}")
    public ApiResponse<FriendDeleteResponse> deleteFriend(
            @RequestHeader("Authorization") String token,
            @PathVariable Long targetId) {
        Long id = getUserIdFromToken(token);
        FriendDeleteResponse data = friendshipService.deleteFriend(id, targetId);
        return ApiResponse.success("친구 삭제 완료", data);
    }
}