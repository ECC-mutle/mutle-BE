package com.mutle.mutle.service;

import com.mutle.mutle.dto.SignupRequestDto;
import com.mutle.mutle.dto.SignupResponseDto;
import com.mutle.mutle.entity.User;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
}
