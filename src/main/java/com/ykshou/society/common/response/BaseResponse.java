package com.ykshou.society.common.response;

import com.ykshou.society.common.BizCodeEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by BG292616 on 2017/5/31.
 */
@Data
public class BaseResponse {
    private State state;

    private String code;

    private String errorMsg;

    private HashMap<Object, Object> props;

    public static BaseResponse error(BizCodeEnum errorEnum) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setState(State.failure);
        baseResponse.setCode("" + errorEnum.getCode());
        baseResponse.setErrorMsg(errorEnum.getMessage());
        return baseResponse;
    }

    public static BaseResponse error(String errorMsg) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setState(State.failure);
        baseResponse.setErrorMsg(errorMsg);
        return baseResponse;
    }

    public static BaseResponse success() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setState(State.success);
        return baseResponse;
    }

    public static BaseResponse create(boolean b) {
        BaseResponse baseResponse = new BaseResponse();
        if (b) {
            baseResponse.setState(State.success);
        } else {
            baseResponse.setState(State.failure);
        }
        return baseResponse;
    }

    public boolean isSuccess() {
        return Objects.equals(this.state, State.success);
    }
}
