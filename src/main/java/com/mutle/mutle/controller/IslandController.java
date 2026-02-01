package com.mutle.mutle.controller;

import com.mutle.mutle.dto.ApiResponse;
import com.mutle.mutle.dto.IslandResponseDto;
import com.mutle.mutle.entity.User;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.repository.UserRepository;
import com.mutle.mutle.service.IslandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.mutle.mutle.jwt.JwtUtil;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/island")
@RequiredArgsConstructor
public class IslandController {
    private final IslandService islandService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public ApiResponse<IslandResponseDto> getIsland(
            @PathVariable String userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestHeader("Authorization") String authHeader
    ) {
        Integer targetYear = (year != null)
                ? year
                : LocalDate.now().getYear();

        Integer targetMonth = (month != null)
                ? month
                : LocalDate.now().getMonthValue();

        User user=userRepository.findByUserId(userId)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        Long currentId=getIdFromToken(authHeader);


        IslandResponseDto data=islandService.getIsland(user.getId(), currentId, targetYear, targetMonth);
        return ApiResponse.success("섬 정보가 성공적으로 조회되었습니다.",data);
    }

    private Long getIdFromToken(@RequestHeader("Authorization") String token){
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }
        Long id=jwtUtil.getId(token.substring(7));
        return id;
    }

}
