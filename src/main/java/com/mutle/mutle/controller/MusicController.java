package com.mutle.mutle.controller;

import com.mutle.mutle.dto.ApiResponse;
import com.mutle.mutle.dto.MusicSearchResult;
import com.mutle.mutle.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/music")
public class MusicController {
    private final MusicService musicService;

    @GetMapping("/search")
    public ApiResponse<List<MusicSearchResult>> searchMusic(@RequestParam String keyword) {
        List<MusicSearchResult> results = musicService.searchMusic(keyword);
        return ApiResponse.success("음악 검색 성공", results);
    }
}

