package com.mutle.mutle.controller;

import com.mutle.mutle.dto.*;
import com.mutle.mutle.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto){
        SignupResponseDto data=authService.signup(requestDto);

        return ApiResponse.success("회원가입이 성공적으로 완료되었습니다.", data);
    }
    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto){
        LoginResponseDto data=authService.login(requestDto);

        return ApiResponse.success("로그인이 성공적으로 완료되었습니다.", data);
    }
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String token){
        authService.logout(token);
        return ApiResponse.success("로그아웃이 성공적으로 완료되었습니다.", null);
    }
    @DeleteMapping("/me")
    public ApiResponse<Void> withdraw(@Valid @RequestBody WithdrawRequestDto requestDto, @RequestHeader("Authorization") String token){
        authService.withdraw(requestDto, token);
        return ApiResponse.success("회원 탈퇴가 성공적으로 완료되었습니다.", null);
    }
}
