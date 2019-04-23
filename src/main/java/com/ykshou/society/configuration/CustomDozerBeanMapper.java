package com.ykshou.society.configuration;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Created by BG242387 on 2018/1/25.
 */
@Configuration
public class CustomDozerBeanMapper {
    @Bean
    public DozerBeanMapper dozerBeanMapper() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.setMappingFiles(Collections.singletonList("classpath:dozer/dozer-mapping.xml"));
        return dozerBeanMapper;
    }
}
