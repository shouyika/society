package com.ykshou.society.common.dozer;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BG242387 on 2018/1/25.
 */
@Service("dozerTools")
public class DozerTools {
    @Autowired
    private Mapper dozerBeanMapper;

    public <T> T clone(T t) {
        if (t == null) {
            return null;
        }
        return (T) dozerBeanMapper.map(t, t.getClass());
    }

    public <T> List<T> cloneList(List<T> list) {
        if (list == null) {
            return null;
        }
        List<T> newList = new ArrayList<>(list.size());
        for (T t : list) {
            newList.add(clone(t));
        }
        return newList;
    }

    /**
     * 转换对象
     *
     * @param s 源对象
     * @param t 目标对象
     */
    public <S, T> T convert(S s, T t) {
        if (s == null) {
            return t;
        }
        dozerBeanMapper.map(s, t);
        return t;
    }

    /**
     * 转换对象
     *
     * @param s    源对象
     * @param type 目标对象类型
     */
    public <S, T> T convert(S s, Class<T> type) {
        if (s == null) {
            return null;
        }
        return dozerBeanMapper.map(s, type);
    }

    /**
     * 转换对象
     *
     * @param list 源对象
     * @param type 目标对象类型
     */
    public <S, T> List<T> convertList(List<S> list, Class<T> type) {
        if (list == null) {
            return null;
        }
        List<T> newList = new ArrayList<>(list.size());
        for (S s : list) {
            newList.add(convert(s, type));
        }
        return newList;
    }
}
