package com.kfyty.security.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class Result<T> {
    private T data;
    private int status;
    private String message;

    public Result(HttpStatus status) {
        this(status, null);
    }

    public Result(T data) {
        this(HttpStatus.OK);
        this.data = data;
    }

    public Result(HttpStatus status, String message) {
        this(null, status.value(), message);
    }

    public static <T> Result<T> test(boolean success) {
        return success ? OK() : FAIL();
    }

    public static <T> Result<T> test(boolean success, String msgIfFailed) {
        return success ? OK() : FAIL(msgIfFailed);
    }

    public static <T> Result<T> OK() {
        return new Result<>(HttpStatus.OK);
    }

    public static <T> Result<T> OK(T data) {
        return OK(data, null);
    }

    public static <T> Result<T> OK(String message) {
        return OK(null, message);
    }

    public static <T> Result<T> OK(T data, String message) {
        return new Result<>(data, HttpStatus.OK.value(), message);
    }

    public static <T> Result<T> FAIL() {
        return FAIL(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> Result<T> FAIL(HttpStatus status) {
        return FAIL(status, null);
    }

    public static <T> Result<T> FAIL(String message) {
        return FAIL(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static <T> Result<T> FAIL(HttpStatus status, String message) {
        return new Result<>(null, status.value(), message);
    }
}
