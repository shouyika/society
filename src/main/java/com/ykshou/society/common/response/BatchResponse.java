package com.ykshou.society.common.response;

import com.ykshou.society.common.BizCodeEnum;
import lombok.Data;

import java.util.List;

/**
 * Created by BG292616 on 2017/5/31.
 */
@Data
public class BatchResponse<V> extends BaseResponse {

    private List<V> result;

    public static <V> BatchResponse<V> createWithResult(List<V> result) {
        BatchResponse resultDTO = new BatchResponse();
        resultDTO.setState(State.success);
        resultDTO.setResult(result);
        return resultDTO;
    }

    public static BatchResponse error(BizCodeEnum errorEnum) {
        BatchResponse response = new BatchResponse();
        response.setState(State.failure);
        response.setCode("" + errorEnum.getCode());
        response.setErrorMsg(errorEnum.getMessage());
        return response;
    }

    public static BatchResponse error(String errorMsg) {
        BatchResponse response = new BatchResponse();
        response.setState(State.failure);
        response.setErrorMsg(errorMsg);
        return response;
    }

    public List<V> getResult() {
        return result;
    }

    public void setResult(List<V> result) {
        this.result = result;
    }
}
