package com.mutle.mutle.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    DUPLICATE_EMAIL(400, "AUTH_001", "이미 가입된 이메일입니다."),
    DUPLICATE_USER_ID(400, "AUTH_002", "이미 사용 중인 아이디입니다."),
    INVALID_USER_ID(400, "AUTH_003", "아이디 형식이 올바르지 않습니다."),
    INVALID_NICKNAME(400, "AUTH_004", "닉네임 형식이 올바르지 않습니다."),
    INVALID_PASSWORD(400, "AUTH_005", "비밀번호 형식이 올바르지 않습니다."),
    INVALID_EMAIL(400, "AUTH_006", "이메일 형식이 올바르지 않습니다."),
    BLANK_LOGIN(400, "AUTH_101", "아이디 혹은 비밀번호를 입력하지 않았습니다."),
    USER_NOT_FOUND(404, "AUTH_103", "존재하지 않는 사용자입니다."),
    PASSWORD_MISMATCH(401, "AUTH_102", "비밀번호가 틀렸습니다."),
    TOKEN_ERROR(401, "AUTH_000", "인증 정보가 유효하지 않습니다."),
    OLD_PASSWORD(400, "AUTH_501", "이전 비밀번호와 동일합니다."),
    SOCIAL_USER_CANNOT_CHANGE_ID(400,"AUTH_601" , "소셜 회원은 아이디 변경이 불가능 합니다."),
    SOCIAL_USER_CANNOT_CHANGE_EMAIL(400,"AUTH_602" , "소셜 회원은 이메일 변경이 불가능 합니다."),
    SOCIAL_USER_CANNOT_CHANGE_PASSWORD(400,"AUTH_603" ,"소셜 회원은 비밀번호 변경이 불가능 합니다." ),


    TODAY_QUEST_NOT_FOUND(404, "QUESTION_001", "오늘의 질문을 찾을 수 없습니다."),
    ALREADY_SENT_TODAY(400, "BOTTLE_001", "이미 오늘 유리병을 보냈습니다."),
    CONTENT_TOO_LONG(400, "BOTTLE_002", "메모는 200자 이내로 작성해주세요."),
    BOTTLE_NOT_ARRIVED(404, "BOTTLE_003", "도착한 유리병이 없습니다."),
    BOTTLE_NOT_FOUND(404, "BOTTLE_004", "존재하지 않는 유리병입니다."),
    REACTION_DUPLICATION(404, "REACTION_001", "이미 반응을 남긴 유리병입니다."),
    BOOKMARK_DUPLICATION(404, "BOOKMARK_001", "이미 저장된 유리병입니다."),
    EXPIRED_BOTTLE(403, "BOOKMARK_002", "이미 만료된 유리병입니다."),
    MUSIC_NOT_FOUND(404, "MUSIC_001", "올바르지 않은 음악 정보입니다."),


    FRIEND_ALREADY_EXISTS(400, "FRIEND_001", "이미 친구인 사용자입니다."),
    FRIEND_REQUEST_ALREADY_SENT(400, "FRIEND_002", "이미 신청을 보냈습니다."),
    FRIEND_REQUEST_ALREADY_RECEIVED(400, "FRIEND_003", "이미 신청을 받았습니다."),
    ALREADY_PROCESSED_REQUEST(400, "FRIEND_004", "이미 처리된 신청입니다."),
    FRIEND_RELATION_NOT_FOUND(404, "FRIEND_005", "삭제할 친구 관계가 존재하지 않습니다."),
    ALREADY_RESPONSE(400, "FRIEND_006", "이미 수락되었거나 취소할 수 없는 상태입니다."),
    INVALID_SEARCH_CONDITION(400, "VALIDATION_001", "잘못된 검색 조건입니다. (type은 EMAIL이나 ID여야 합니다.)"),
    CANNOT_CANCEL_OTHERS_REQUEST(403, "AUTH_701", "본인이 보낸 신청만 취소할 수 있습니다."),
    REQUEST_NOT_FOUND(404, "REQUEST_001", "존재하지 않는 신청 정보입니다."),
    INVALID_KEYWORD_FORMAT(400, "VALIDATION_002", "올바른 형식의 키워드가 아닙니다."),

    INVALID_YEAR_FORMAT(400, "ISLAND_001","잘못된 연도 형식입니다." ),
    INVALID_MONTH_FORMAT(400, "ISLAND_002","잘못된 월 형식입니다." ),
    INVALID_BIO_FORMAT(400, "ISLAND_101","자기소개는 50자 이내로 작성해주세요." ),
    BLANK_MUSIC_NAME(400, "ISLAND_201", "곡 명을 입력하지 않았습니다."),
    BLANK_ARTIST_NAME(400, "ISLAND_202", "아티스트 명을 입력하지 않았습니다."),
    BLANK_PLATFORM_NAME(400, "ISLAND_301", "플랫폼 명을 입력하지 않았습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
