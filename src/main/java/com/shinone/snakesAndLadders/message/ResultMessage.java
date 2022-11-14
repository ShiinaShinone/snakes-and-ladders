package com.shinone.snakesAndLadders.message;

import lombok.Data;

/**
 * @author 杨泽南
 * @version 1.0.0
 * CreateDate 2022/11/14 15:09
 * description.
 */
@Data
public class ResultMessage<T> {
    private final T data;
    private String msg;
    private Integer code;

    public ResultMessage(T t) {
        this.data = t;
    }

    public ResultMessage(T t, int code) {
        this.data = t;
        this.code = code;
    }

    public ResultMessage(T t, int code, String msg) {
        this.data = t;
        this.code = code;
        this.msg = msg;
    }

    public ResultMessage(T t, int code, String msg, String method) {
        this.data = t;
        this.code = code;
        this.msg = msg;
    }


    public static <T> ResultMessage<T> success(T t) {
        return new ResultMessage<>(t, ResultCodeEnum.SUCCESS.getCode(), "操作成功");
    }

    public static <T> ResultMessage<T> success(String method, T t) {
        return new ResultMessage<>(t, ResultCodeEnum.SUCCESS.getCode(), "操作成功", method);
    }

    public static <T> ResultMessage<T> success() {
        return success(null);
    }

    public static <T> ResultMessage<T> error(String msg) {
        return new ResultMessage<>(null, ResultCodeEnum.FAIL.getCode(), msg);
    }

    public static <T> ResultMessage<T> error(String method, String msg) {
        return new ResultMessage<>(null, ResultCodeEnum.FAIL.getCode(), msg, method);
    }

    public static <T> ResultMessage<T> error() {
        return error(null);
    }
}

