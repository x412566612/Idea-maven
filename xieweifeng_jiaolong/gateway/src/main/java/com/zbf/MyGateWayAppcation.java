package com.zbf;/*
@author 谢唯峰
@create 2019-08-05-11:45
*/

import com.zbf.config.MyReslover;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MyGateWayAppcation {
    public static void main(String[] args) {
        SpringApplication.run(MyGateWayAppcation.class,args);
    }

    @RequestMapping("serverhealth")
    public String serverhealth(){
        System.out.println("=========gateway server check health is ok! ^_^ ========");
        return "ok";
    }

    @Bean(name="myAddrReslover")
    public MyReslover getMyReslover(){
        return new MyReslover();
    }
}
