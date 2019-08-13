package com.xieweifeng.config;/*
@author 谢唯峰
@create 2019-07-22-16:55
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
public class SwggerConfig {
    @Bean
    public Docket getDocket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        //初始化配置
        docket.apiInfo(getApiInfo());

        docket.select().apis(
                RequestHandlerSelectors.basePackage("com.xieweifeng.web")
        ).paths(
                PathSelectors.ant("/**")
        ).build();

        docket.ignoredParameterTypes(String.class);
        return docket;
    }

    @Bean
    public ApiInfo getApiInfo(){
        Contact contact = new Contact(
                "姓名"
                , "http://www.baidu.com"
                , "412566612@qq.com"
        );
        ApiInfo apiInfo = new ApiInfo(
                "标题"
                , "说明"
                , "v-1.0版本"
                , "http://www.baidu.com"
                , contact
                , "许可证名称"
                , "http://www.baidu.com"
                , new ArrayList<VendorExtension>()
        );
        return apiInfo;
    }
}
