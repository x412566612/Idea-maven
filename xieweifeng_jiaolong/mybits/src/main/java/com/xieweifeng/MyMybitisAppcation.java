package com.xieweifeng;/*
@author 谢唯峰
@create 2019-08-07-14:00
*/

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan(value = "com.xieweifeng.dao")
@RestController
public class MyMybitisAppcation {
    public static void main(String[] args) {
        SpringApplication.run(MyMybitisAppcation.class,args);
    }

    @RequestMapping("serverhealth")
    public String serverhealth(){
        System.out.println("=========gateway server check health is ok! ^_^ ========");
        return "ok";
    }
}
