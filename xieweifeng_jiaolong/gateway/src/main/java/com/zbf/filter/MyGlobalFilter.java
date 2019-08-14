package com.zbf.filter;


import com.alibaba.fastjson.JSONObject;
import com.xieweifeng.jwt.JWTUtils;
import com.xieweifeng.utils.TwitterIdWorker;
import com.zbf.myexception.MyException;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤器
 */
@Component
public class MyGlobalFilter implements GlobalFilter {

    @Value("${my.auth.urls}")
    @ApiModelProperty(value = "放行的请求")
    private String[] urls;

    @Value("${my.auth.loginPath}")
    @ApiModelProperty(value = "重定向地址")
    private String loginpage;

    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
       ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().toString();//获取拦截到的请求
        List<String> list = Arrays.asList(urls);//取得自动放行的地址
        if(list.contains(uri)){//判断请求是否自动放行
            return chain.filter(exchange);
        }else{
            List<String> listToken = request.getHeaders().get("token");//获取请求头中的token
            String token = listToken.get(0);
            JSONObject jsonObject=null;
            try {
                //通过解密token,判断token是否可用,解密失败则抛异常
                jsonObject = JWTUtils.decodeJwtTocken(token);
                //解密成功,则刷新token持续时间
                String s = JWTUtils.generateToken(jsonObject.toString());
                response.getHeaders().set("token",s);
            }catch (MyException e){
                //解密失败抛异常
                e.printStackTrace();
                /*j.printStackTrace();*/
                response.getHeaders().set("Location",loginpage);
                response.setStatusCode(HttpStatus.SEE_OTHER);
                return exchange.getResponse().setComplete();
            }
            //获取redis缓存内,当前用户的访问权限
            //将获取到的权限与请求进行对比,判断是否有权限访问指定服务
            Boolean hasKey = redisTemplate.opsForHash().hasKey("USERDATAAUTH" + jsonObject.get("id"), uri);
            if(hasKey){
                return chain.filter(exchange);
            }else {
                throw new MyException("不能访问该资源 !");
            }

        }
    }
}
