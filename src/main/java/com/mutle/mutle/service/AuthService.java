package com.mutle.mutle.service;

import com.mutle.mutle.dto.*;
import com.mutle.mutle.entity.User;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.jwt.JwtUtil;
import com.mutle.mutle.jwt.TokenBlacklist;
import com.mutle.mutle.repository.*;
import org.springframework.security.core.parameters.P;
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
        //아이디 중복 확인
        checkUserId(requestDto.getUserId());
        //이메일 중복 확인
        checkEmail(requestDto.getEmail());

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


    //회원탈퇴
    @Transactional
    public void withdraw(WithdrawRequestDto requestDto, String authHeader){

        //유저 조회
        User user=userRepository.findByUserId(requestDto.getUserId()).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        //비밀번호 일치 확인
        if(!passwordEncoder.matches(requestDto.getPassword(),user.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        //토큰 만료시키기
         logout(authHeader);

        userRepository.delete(user);
    }

    //아이디 중복 확인
    @Transactional(readOnly = true)
    public void checkUserId(String userId) {
        if (userRepository.existsByUserId(userId)){
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }
    }

    //이메일 중복 확인
    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        if(userRepository.existsByEmail(email)){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    //정보 조회
    @Transactional(readOnly = true)

    public UserInfoResponseDto userInfo(Long id) {
        User user=userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserInfoResponseDto(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImage()
        );
    }


    //정보 수정
    @Transactional
    public UserInfoResponseDto userInfoFix(UserInfoRequestDto requestDto, Long id) {
    User user=userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        //아이디 변경
        if(requestDto.getUserId() != null &&!user.getUserId().equals(requestDto.getUserId())){
            checkUserId(requestDto.getUserId());
            user.updateUserId(requestDto.getUserId());
        }

        //이메일 변경
        if(requestDto.getEmail() != null &&!user.getEmail().equals(requestDto.getEmail())){
            checkEmail(requestDto.getEmail());
            user.updateEmail(requestDto.getEmail());
        }

        //닉네임 변경
        if(requestDto.getNickname()!=null && !user.getNickname().equals(requestDto.getNickname())){
            user.updateNickname(requestDto.getNickname());
        }

        //이미지 변경
        if(requestDto.getProfileImage()!=null && !user.getProfileImage().equals(requestDto.getProfileImage())){
            user.updateProfileImage(requestDto.getProfileImage());
        }

        return new UserInfoResponseDto(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImage()
        );
    }

    //비밀번호 수정
    @Transactional
    public void passwordUpdate(PasswordUpdateRequestDto requestDto, Long id){
        User user=userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        //비밀번호 일치 검사
        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        //기존 비밀번호와 다른지 검사
        if(passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.OLD_PASSWORD);
        }

        String encodedPassword=passwordEncoder.encode(requestDto.getNewPassword());
        user.updatePassword(encodedPassword);
    }
}