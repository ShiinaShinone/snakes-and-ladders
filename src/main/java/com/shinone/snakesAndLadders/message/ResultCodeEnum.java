package com.shinone.snakesAndLadders.message;

/**
 * @author 杨泽南
 * @version 1.0.0
 * CreateDate 2022/11/14 15:10
 * description.
 */
public enum ResultCodeEnum {
    SUCCESS("成功", 0, "操作成功."),
    FAIL("失败", -1, "您的请求发生了异常."),
    PARAM("数据格式错误", -4, "请求参数错误.");
    private int code;
    private String name;
    private String message;

    private ResultCodeEnum(String name, int code) {
        this(name, code, "");
    }

    private ResultCodeEnum(String name, int code, String message) {
        this.name = name;
        this.code = code;
        this.message = message;
    }

    public String getName() {
        return this.name;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
