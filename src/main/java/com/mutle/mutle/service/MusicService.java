package com.mutle.mutle.service;

import com.mutle.mutle.dto.ItunesResponse;
import com.mutle.mutle.dto.MusicSearchResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class MusicService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<MusicSearchResult> searchMusic(String keyword) {
        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

            // 검색
            String url = "https://itunes.apple.com/search?term=" + encodedKeyword +
                    "&entity=song&country=kr&limit=20";

            // API 호출 및 결과
            ItunesResponse response = restTemplate.getForObject(url, ItunesResponse.class);

            // 반환
            return (response != null) ? response.results() : Collections.emptyList();

        } catch (Exception e) {
            System.err.println("iTunes API 호출 중 에러 발생: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
