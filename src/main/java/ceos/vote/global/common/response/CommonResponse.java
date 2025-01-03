package ceos.vote.global.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResponse<T> {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private int code;
    private String message;
    private T result;

    // 반환값 있는 경우
    public CommonResponse(T result, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = SuccessCode.SUCCESS.getCode();
        this.message = message;
        this.result = result;
    }

    // 반환값 없는 경우
    public CommonResponse(String message) {
        this.timestamp = LocalDateTime.now();
        this.code = SuccessCode.SUCCESS.getCode();
        this.message = message;
    }
}
