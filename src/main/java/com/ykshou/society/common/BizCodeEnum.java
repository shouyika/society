package com.ykshou.society.common;

/**
 * Created by BG242387 on 2018/1/3.
 */
public enum BizCodeEnum {
    //100 ~ 300
    ;

    private int code;

    private String message;

    BizCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
