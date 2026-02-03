package com.mutle.mutle.controller;

import com.mutle.mutle.dto.*;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.jwt.JwtUtil;
import com.mutle.mutle.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

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
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String authHeader){
        authService.logout(authHeader);
        return ApiResponse.success("로그아웃이 성공적으로 완료되었습니다.", null);
    }
    @DeleteMapping("/me")
    public ApiResponse<Void> withdraw(@Valid @RequestBody WithdrawRequestDto requestDto, @RequestHeader("Authorization") String token){
        Long id = getUserIdFromToken(token);
        authService.withdraw(requestDto, token, id);
        return ApiResponse.success("회원 탈퇴가 성공적으로 완료되었습니다.", null);
    }
    @GetMapping("/check-user-id")
    public ApiResponse<Void> checkUserId(@RequestParam String userId){
        authService.checkUserId(userId);
        return ApiResponse.success("사용 가능한 아이디입니다.", null);
    }
    @GetMapping("/check-email")
    public ApiResponse<Void> checkEmail(@RequestParam String email){
        authService.checkEmail(email);
        return ApiResponse.success("사용 가능한 이메일입니다.", null);
    }
    @GetMapping("/me")
    public ApiResponse<UserInfoResponseDto> userInfo(@RequestHeader("Authorization") String token){
        Long id = getUserIdFromToken(token);
        UserInfoResponseDto data=authService.userInfo(id);
        return ApiResponse.success("정보를 성공적으로 조회했습니다.", data);
    }
    @PatchMapping("/me") public ApiResponse<UserInfoResponseDto> userInfoFix(@Valid @RequestBody UserInfoRequestDto requestDto, @RequestHeader("Authorization") String token){
        Long id = getUserIdFromToken(token);
        UserInfoResponseDto data=authService.userInfoFix(requestDto, id);
        return ApiResponse.success("정보를 성공적으로 수정했습니다.", data);
    }
    @PutMapping("/me/password") public ApiResponse<Void> passwordUpdate(@Valid @RequestBody PasswordUpdateRequestDto requestDto, @RequestHeader("Authorization") String token){
        Long id = getUserIdFromToken(token);
        authService.passwordUpdate(requestDto, id);
        return  ApiResponse.success("비밀번호가 성공적으로 변경되었습니다.", null);
    }

    private Long getUserIdFromToken(String token){
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
            }
        Long id=jwtUtil.getId(token.substring(7));
        return id;
    }

    @GetMapping("/kakao")
    public ApiResponse<LoginResponseDto> kakaoLogin(@RequestParam String code) {
        LoginResponseDto data = authService.kakaoLogin(code);

        return ApiResponse.success("카카오 로그인이 성공적으로 완료되었습니다.", data);
    }

    @PostMapping("/google")
    public ApiResponse<LoginResponseDto> googleLogin(@RequestBody SocialLoginRequestDto requestDto) {
        LoginResponseDto data = authService.googleLogin(requestDto.getCode());
        return ApiResponse.success("구글 로그인이 성공적으로 완료되었습니다.", data);
    }
}

