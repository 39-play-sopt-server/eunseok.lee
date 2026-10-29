package org.sopt;

public class ApiResponse<T> {

    private final String message;
    private final T data;

    //공통 응답 객체. 실패 또는 성공 메세지와 게시글 데이터를 반환
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }
}