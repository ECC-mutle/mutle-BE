package com.mutle.mutle.service;

import com.mutle.mutle.dto.ItunesResponse;
import com.mutle.mutle.dto.MusicSearchResult;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class MusicService {

    private final RestTemplate restTemplate;

    public MusicService() {
        this.restTemplate = new RestTemplate();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(
                new MediaType("text", "javascript", StandardCharsets.UTF_8)));

        this.restTemplate.getMessageConverters().add(converter);
    }

    public List<MusicSearchResult> searchMusic(String keyword) {
        try {
            if (keyword == null || keyword.isBlank()) {
                return Collections.emptyList();
            }

            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

            // iTunes Search API URL
            String url = "https://itunes.apple.com/search?term=" + encodedKeyword +
                    "&entity=song&country=kr&limit=20";

            // API 호출
            ItunesResponse response = restTemplate.getForObject(url, ItunesResponse.class);

            return (response != null) ? response.results() : Collections.emptyList();

        } catch (Exception e) {
            System.err.println("iTunes API 호출 중 에러 발생: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
