package dorosee.initial.config.basestatus;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * Basic HTTP StatusCode
     */
    FORBIDDEN(false, 403, "Forbidden"),
    NOT_FOUND(false, 404, "Not Found"),

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */

    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    NOT_FOUND_JWT_TOKEN(false, 2001, "토큰을 발견할 수 없습니다.."),
    EXPIRED_TOKEN(false, 2002, "유효하지 않은 토큰입니다."),
    INVALID_USER_JWT(false, 2003, "권한이 없는 유저의 접근입니다."),
    FAIL_TO_ROLE_CHECK(false, 2004, "권한이 없는 유저의 접근입니다."),
    EXCEPTION_TOKEN_EXPIRED(false, 2005, "만료된 토큰입니다."),
    EXCEPTION_WRONG_TOKEN(false, 2006, "잘못된 토큰입니다."),


    //2100 USER
    EXIST_BY_USER_ID(false, 2100, "해당 ID를 가진 유저가 이미 존재합니다."),
    NOT_FOUND_BY_USER_ID(false, 2101, "해당 ID로 찾을 수 있는 유저 정보가 없습니다"),
    NOT_FOUND_ANY_USER(false, 2102, "user table에 유저가 존재하지 않습니다."),
    FAIL_TO_DELETE_USER(false, 2103, "해당 유저를 삭제하는데 실패했습니다."),
    FAIL_TO_UPDATE_USER(false, 2104, "해당 유저정보를 수정하는데 실패했습니다."),

    /**
     * 3000 : Response 오류
     */

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.");

    //[PATCH] /users/{userIdx}

    //Post /posts

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

