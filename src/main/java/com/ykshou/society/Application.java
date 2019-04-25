package com.ykshou.society;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Created by BG242387 on 2017/12/8.
 */
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void init() {
        System.out.println(dataSource);
    }
}
