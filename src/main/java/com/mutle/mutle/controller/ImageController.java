package com.mutle.mutle.controller;

import com.mutle.mutle.dto.ApiResponse;
import com.mutle.mutle.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {


    private final ImageService imageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl=imageService.upload(file);
        return ApiResponse.success("이미지가 성공적으로 업로드되었습니다.", imageUrl);
    }
}
