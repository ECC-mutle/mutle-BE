package com.mutle.mutle.controller;

import com.mutle.mutle.dto.ApiResponse;
import com.mutle.mutle.dto.MenuResponseDto;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.jwt.JwtUtil;
import com.mutle.mutle.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ApiResponse<MenuResponseDto> getMenu(@RequestHeader("Authorization") String token){
        Long id = getIdFromToken(token);
        MenuResponseDto data=menuService.getMenu(id);
        return ApiResponse.success("정보를 성공적으로 조회했습니다.", data);
    }

    private Long getIdFromToken(String token){
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }
        Long id=jwtUtil.getId(token.substring(7).trim());
        return id;
    }
}
