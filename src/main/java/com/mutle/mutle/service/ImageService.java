package com.mutle.mutle.service;

import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket.mutle}")
    private String mutleBucket;

    private final WebClient webClient;

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_FILE);
        }

        //파일명 생성
        String path = mutleBucket + "/" + generateSafeFileName(file.getOriginalFilename());

        //업로드
        webClient.post()
                .uri(supabaseUrl + "/storage/v1/object/" + path)
                .header("Authorization", "Bearer " + supabaseKey)
                .header("apiKey", supabaseKey)
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .bodyValue(getFileBytes(file))
                .retrieve()
                .onStatus(status -> status.isError(), response -> {
                    throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
                })
                .bodyToMono(String.class)
                .block();
        return supabaseUrl + "/storage/v1/object/public/" + path;
    }

    private String generateSafeFileName(String originalFilename) {
        String extension = (originalFilename != null && originalFilename.contains(".")) ?
                originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        return UUID.randomUUID() + extension;
    }

    private byte[] getFileBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
        }
    }
}
