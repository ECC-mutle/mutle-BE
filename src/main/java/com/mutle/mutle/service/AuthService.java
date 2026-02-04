package com.mutle.mutle.service;

import com.mutle.mutle.dto.*;
import com.mutle.mutle.entity.User;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.jwt.JwtUtil;
import com.mutle.mutle.jwt.TokenBlacklist;
import com.mutle.mutle.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.Objects;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenBlacklist tokenBlacklist;
    private final RestTemplate restTemplate;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, TokenBlacklist tokenBlacklist, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklist = tokenBlacklist;
        this.restTemplate = restTemplate;
    }

    @Value("${kakao.admin-key}")
    private String kakaoAdminKey;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-url}")
    private String kakaoRedirectUrl;

    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-url}")
    private String googleRedirectUrl;

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
                .orElseThrow(()->new CustomException(ErrorCode.AUTH_103));

        //비밀번호 일치 확인
        if(!passwordEncoder.matches(requestDto.getPassword(),user.getPassword())){
            throw new CustomException(ErrorCode.AUTH_102);
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
    public void withdraw(WithdrawRequestDto requestDto, String token, Long id){

        //유저 조회
        User user=userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.AUTH_103));

        //비밀번호 일치 확인
        if (!user.getUserId().startsWith("kakao_")) {
            if (requestDto.getPassword() == null || !passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.AUTH_102);
            }
        }

        if (user.getUserId().startsWith("kakao_")) {
            unlinkKakaoUser(user.getUserId().replace("kakao_", ""));
        }

        //토큰 만료시키기
         logout(token);

        userRepository.delete(user);
    }

    private void unlinkKakaoUser(String kakaoUserId) {
        String url = "https://kapi.kakao.com/v1/user/unlink";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoAdminKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type", "user_id");
        params.add("target_id", kakaoUserId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            restTemplate.postForObject(url, request, String.class);
        } catch (Exception e) {
            System.out.println("카카오 연동 해제 실패: " + e.getMessage());
        }
    }

    //아이디 중복 확인
    @Transactional(readOnly = true)
    public void checkUserId(String userId) {
        if (userRepository.existsByUserId(userId)){
            throw new CustomException(ErrorCode.AUTH_002);
        }
    }

    //이메일 중복 확인
    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        if(userRepository.existsByEmail(email)){
            throw new CustomException(ErrorCode.AUTH_001);
        }
    }

    //정보 조회
    @Transactional(readOnly = true)

    public UserInfoResponseDto userInfo(Long id) {
        User user=userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.AUTH_103));

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
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.AUTH_103));


        boolean isSocialUser = user.getUserId().startsWith("kakao_") || user.getUserId().startsWith("google_");

        //아이디 변경
        if (requestDto.getUserId() != null && !user.getUserId().equals(requestDto.getUserId())) {
            if (isSocialUser) throw new CustomException(ErrorCode.AUTH_601); // 에러코드 하나 만드세요!
            checkUserId(requestDto.getUserId());
            user.updateUserId(requestDto.getUserId());
        }

        //이메일 변경
        if (requestDto.getEmail() != null && !user.getEmail().equals(requestDto.getEmail())) {
            if (isSocialUser) throw new CustomException(ErrorCode.AUTH_602);
            checkEmail(requestDto.getEmail());
            user.updateEmail(requestDto.getEmail());
        }


            //닉네임 변경
        if (requestDto.getNickname() != null && !user.getNickname().equals(requestDto.getNickname())) {
            user.updateNickname(requestDto.getNickname());
        }

        //이미지 변경
        if (requestDto.getProfileImage() != null && !Objects.equals(user.getProfileImage(), requestDto.getProfileImage())) {
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
        User user=userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.AUTH_103));

        boolean isSocialUser = user.getUserId().startsWith("kakao_") || user.getUserId().startsWith("google_");

        if (isSocialUser) {
            throw new CustomException(ErrorCode.AUTH_603);
        }

        //비밀번호 일치 검사
        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.AUTH_102);
        }

        //기존 비밀번호와 다른지 검사
        if(passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.AUTH_501);
        }

        String encodedPassword=passwordEncoder.encode(requestDto.getNewPassword());
        user.updatePassword(encodedPassword);
    }

    //카카오 로그인

    @Transactional
    public LoginResponseDto kakaoLogin(String code){
        //access token 요청
        String accessToken=getAccessTokenfromKakao(code);

        //access token으로 사용자 정보 가져오기
        KakaoUserInfoDto kakaoUser=getKakaoUserInfo(accessToken);

        //db에 유저 있으면 조회, 없으면 회원가입
        boolean isNewUser = !userRepository.existsByEmail(kakaoUser.getEmail());

        User user=userRepository.findByEmail(kakaoUser.getEmail())
                .orElseGet(()->{
                    User newUser=User.builder()
                            .userId("kakao_"+kakaoUser.getUserId()) //kakao 회원가입시 임의 id
                            .email(kakaoUser.getEmail())
                            .nickname(kakaoUser.getNickname())
                            .password(passwordEncoder.encode("KAKAO_LOGIN_" + Math.random())) //카카오 로그인은 비밀번호가 필요 없지만 db 제약을 위해 난수 생성
                            .build();
                    return userRepository.save(newUser);
                });

        //jwt  토큰 발급
        String jwtAccessToken=jwtUtil.generateAccessToken(user.getId());
        String jwtRefreshToken=jwtUtil.generateRefreshToken(user.getId());

        return new LoginResponseDto(jwtAccessToken, jwtRefreshToken, user.getUserId(), isNewUser);
    }

    private String getAccessTokenfromKakao(String code){
        String url="https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params=new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUrl);
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
        return (String) response.get("access_token");
    }
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        Map<String, Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return new KakaoUserInfoDto(
                String.valueOf(response.get("id")),
                (String) kakaoAccount.get("email"),
                (String) profile.get("nickname")
        );
    }

    // 구글 로그인 (AuthService에 추가)
    @Transactional
    public LoginResponseDto googleLogin(String code) {
        //Access Token 받기
        String accessToken = getAccessTokenFromGoogle(code);

        //유저 정보 가져오기
        GoogleUserInfoDto googleUser = getGoogleUserInfo(accessToken);

        //db에 유저 있으면 조회, 없으면 회원가입
        boolean isNewUser = !userRepository.existsByEmail(googleUser.getEmail());

        User user = userRepository.findByEmail(googleUser.getEmail())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .userId("google_" + googleUser.getSub())
                            .email(googleUser.getEmail())
                            .nickname(googleUser.getName())
                            .password(passwordEncoder.encode("GOOGLE_LOGIN_" + Math.random()))
                            .build();
                    return userRepository.save(newUser);
                });

        String jwtAccessToken = jwtUtil.generateAccessToken(user.getId());
        String jwtRefreshToken = jwtUtil.generateRefreshToken(user.getId());

        return new LoginResponseDto(jwtAccessToken, jwtRefreshToken, user.getUserId(), isNewUser);
    }

    private String getAccessTokenFromGoogle(String code) {
        
        String url = "https://oauth2.googleapis.com/token";


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUrl);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    private GoogleUserInfoDto getGoogleUserInfo(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 구글 유저 정보 응답 받기
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> body = response.getBody();

        return new GoogleUserInfoDto(
                String.valueOf(body.get("sub")),
                (String) body.get("email"),
                (String) body.get("name")
        );
    }
}
