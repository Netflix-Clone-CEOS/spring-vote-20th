package ceos.vote.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // 1000: Success Case

    // 2000: Common Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 2000, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요."),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, 2001, "존재하지 않는 리소스입니다."),
    INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, 2002, "올바르지 않은 요청 값입니다."),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, 2003, "권한이 없는 요청입니다."),
    ALREADY_DELETE_EXCEPTION(HttpStatus.BAD_REQUEST, 2004, "이미 삭제된 리소스입니다."),
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, 2005, "인가되지 않는 요청입니다."),
    ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, 2006, "이미 존재하는 리소스입니다."),
    INVALID_SORT_EXCEPTION(HttpStatus.BAD_REQUEST, 2007, "올바르지 않은 정렬 값입니다."),
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, 2008, "잘못된 요청입니다."),

    // 3000: Auth Error
    KAKAO_TOKEN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 3000, "토큰 발급에서 오류가 발생했습니다."),
    KAKAO_USER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 3001, "Kakao 프로필 정보를 가져오는 과정에서 오류가 발생했습니다."),
    WRONG_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, 3002, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, 3003,"올바르지 않은 형식의 RefreshToken 입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, 3004,"올바르지 않은 형식의 AccessToken 입니다."),
    DUPLICATED_ADMIN_USERNAME(HttpStatus.BAD_REQUEST, 3005,"중복된 사용자 이름입니다."),
    DUPLICATED_ADMIN_EMAIL(HttpStatus.BAD_REQUEST, 3006,"중복된 사용자 이메일입니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, 3007,"존재하지 않는 RefreshToken 입니다."),
    EXPIRED_PERIOD_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 3008,"기한이 만료된 RefreshToken 입니다."),
    EXPIRED_PERIOD_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 3009,"기한이 만료된 AccessToken 입니다."),
    NOT_FOUND_REFRESH_TOKEN_IN_DB(HttpStatus.NOT_FOUND, 3010,"현재 DB에 존재하지 않는 RefreshToken 입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 3011,"존재하지 않는 사용자입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}