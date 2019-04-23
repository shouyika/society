package com.ykshou.society.configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.ykshou.society.web.formatter.StringDateFormatAnnotationFormatterFactory;
import com.ykshou.society.web.formatter.StringDateTimeFormatAnnotationFormatterFactory;
import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.HashMap;

/**
 * Created by BG242387 on 2018/2/7.
 */
@Configuration
public class CustomWebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new StringDateFormatAnnotationFormatterFactory());
        registry.addFormatterForFieldAnnotation(new StringDateTimeFormatAnnotationFormatterFactory());
        super.addFormatters(registry);
    }

    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("resetEnable", "true");
        parametersMap.put("loginUsername", "admin");
        parametersMap.put("loginPassword", "admin");
        servletRegistrationBean.setInitParameters(parametersMap);
        servletRegistrationBean.setName("DruidStatView");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean securityFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setName("springSecurityFilterChain");
        filterRegistrationBean.setFilter(new DelegatingFilterProxy());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(200);
        return filterRegistrationBean;
    }
}
