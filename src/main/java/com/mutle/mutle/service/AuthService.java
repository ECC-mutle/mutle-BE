package com.mutle.mutle.service;

import com.mutle.mutle.dto.LoginRequestDto;
import com.mutle.mutle.dto.LoginResponseDto;
import com.mutle.mutle.dto.SignupRequestDto;
import com.mutle.mutle.dto.SignupResponseDto;
import com.mutle.mutle.entity.User;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.jwt.JwtUtil;
import com.mutle.mutle.jwt.TokenBlacklist;
import com.mutle.mutle.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenBlacklist tokenBlacklist;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, TokenBlacklist tokenBlacklist) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklist = tokenBlacklist;
    }

    //회원가입
    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto){
        //아이디 중복 검사
        if(userRepository.existsByUserId(requestDto.getUserId())){
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }
        //이메일 중복 검사
        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        //유저 객체 생성
        User user=User.builder()
                .userId(requestDto.getUserId())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .profileImage(requestDto.getProfileImage())
                .build();

        //db 저장
        User savedUser=userRepository.save(user);

        //dto 생성
        return new SignupResponseDto(savedUser.getCreatedAt());
    }


    //로그인
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto){
        //db에서 유저 찾기
        User user=userRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        //비밀번호 일치 확인
        if(!passwordEncoder.matches(requestDto.getPassword(),user.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken= jwtUtil.generateAccessToken(user.getId());
        String refreshToken= jwtUtil.generateRefreshToken(user.getId());

        return new LoginResponseDto(
                accessToken,
                refreshToken,
                user.getUserId(),
                false
        );
    }

    //로그아웃
    public void logout(String authHeader){
        if(authHeader!=null && authHeader.startsWith("Bearer ")){ //토큰 있음
            String token=authHeader.substring(7);
            tokenBlacklist.addBlackList(token);
        }

    }


}
