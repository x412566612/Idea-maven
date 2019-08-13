package com.xieweifeng;/*
@author 谢唯峰
@create 2019-08-05-11:57
*/

import com.xieweifeng.exception.LoginException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SpringBootApplication
@EnableSwagger2
@MapperScan(value = "com.xieweifeng.dao")
public class MyTokenAppcation {
    public static void main(String[] args) throws LoginException {
        SpringApplication.run(MyTokenAppcation.class,args);
    }
}
