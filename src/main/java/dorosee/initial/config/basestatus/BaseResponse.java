package dorosee.initial.config.basestatus;

import lombok.Getter;

import java.util.Map;

@Getter
public class BaseResponse<T> {
    private final Boolean isSuccess;
    private final String message;
    private final int code;
    private T result;
    private T results;
    private Map<String, Object> totalMap;

    // 요청에 성공한 경우
    public BaseResponse(T result, T results) {
        this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.result = result;
        this.results = results;
    }

    // 요청에 성공한 경우
    public BaseResponse(T result, T results, Map<String, Object> totalMap) {
        this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.result = result;
        this.results = results;
        this.totalMap = totalMap;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }
}

