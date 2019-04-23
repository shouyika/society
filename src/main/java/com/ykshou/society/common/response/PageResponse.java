package com.ykshou.society.common.response;

import lombok.Data;

import java.util.List;

/**
 * Created by BG292616 on 2017/5/31.
 */
@Data
public class PageResponse<V> extends BatchResponse<V> {
    private long total;

    public static <V> PageResponse<V> createWithPage(long total, List<V> resultList) {
        PageResponse pageResultDTO = new PageResponse();
        pageResultDTO.setTotal(total);
        pageResultDTO.setResult(resultList);
        pageResultDTO.setState(State.success);
        return pageResultDTO;
    }
}
