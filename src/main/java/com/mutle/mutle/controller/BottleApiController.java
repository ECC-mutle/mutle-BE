package com.mutle.mutle.controller;

import com.mutle.mutle.dto.*;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.jwt.JwtUtil;
import com.mutle.mutle.service.BottleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bottles")
@RequiredArgsConstructor
public class BottleApiController {

    private final BottleService bottleService;
    private final JwtUtil jwtUtil;

    private Long getUserIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }
        return jwtUtil.getId(token.substring(7));
    }

    // 유리병 보내기
    @PostMapping
    public ApiResponse<BottleCreateResponse> createBottle(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody BottleCreateRequest request) {
        Long id = getUserIdFromToken(token);
        BottleCreateResponse data = bottleService.createBottle(id, request.getQuestionId(), request.getMusicInfo().getMusicId(), request);
        return ApiResponse.success("유리병을 성공적으로 보냈습니다.", data);
    }

    // 유리병 받기
    @GetMapping("/random")
    public ApiResponse<BottleRandomResponse> getRandomBottle(@RequestHeader(value = "Authorization", required = false) String token) {
        Long id = getUserIdFromToken(token);
        BottleRandomResponse data = bottleService.getBottle(id);
        return ApiResponse.success("유리병 획득 성공", data);
    }

    // 오늘의 질문 조회
    @GetMapping("/todayQuest")
    public ApiResponse<TodayQuestResponse> getTodayQuest() {
        TodayQuestResponse data = bottleService.getTodayQuest();
        return ApiResponse.success("오늘의 질문 조회 성공", data);
    }

    // 유리병 상세페이지 조회
    @GetMapping("/{bottleId}")
    public ApiResponse<BottleDetailResponse> getBottleDetail(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long bottleId) {
        Long id = getUserIdFromToken(token);
        BottleDetailResponse data = bottleService.getBottleDetail(bottleId, id);
        return ApiResponse.success("상세 조회 성공", data);
    }

    // 반응 남기기
    @PostMapping("/{bottleId}/reaction")
    public ApiResponse<BottleReactionCreateResponse> addReaction(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long bottleId) {
        Long id = getUserIdFromToken(token);
        BottleReactionCreateResponse data = bottleService.addReaction(id, bottleId);
        return ApiResponse.success("반응 남기기 성공", data);
    }

    // 반응 조회
    @GetMapping("/{bottleId}/reaction")
    public ApiResponse<BottleReactionGetResponse> getReactions(@PathVariable Long bottleId) {
        BottleReactionGetResponse data = bottleService.getReactions(bottleId);
        return ApiResponse.success("반응 조회 성공", data);
    }

    // 북마크 추가
    @PostMapping("/{bottleId}/bookmark")
    public ApiResponse<BookmarkCreateResponse> addBookmark(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long bottleId) {
        Long id = getUserIdFromToken(token);
        BookmarkCreateResponse data = bottleService.addBookmark(bottleId, id);
        return ApiResponse.success("북마크 저장 성공", data);
    }

    // 북마크 목록 조회
    @GetMapping("/bookmarks")
    public ApiResponse<List<BookmarkListResponse>> getBookmarks(@RequestHeader(value = "Authorization", required = false) String token) {
        Long id = getUserIdFromToken(token);
        List<BookmarkListResponse> data = bottleService.getBookmarks(id);
        return ApiResponse.success("북마크 목록 조회 성공", data);
    }
}