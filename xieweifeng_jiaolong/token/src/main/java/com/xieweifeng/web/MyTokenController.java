package com.xieweifeng.web;/*
@author 谢唯峰
@create 2019-08-05-11:58
*/

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xieweifeng.dao.UserDao;
import com.xieweifeng.entity.ResponseResult;
import com.xieweifeng.entity.UserInfo;
import com.xieweifeng.exception.LoginException;
import com.xieweifeng.jwt.JWTUtils;
import com.xieweifeng.myexception.MyException;
import com.xieweifeng.randm.VerifyCodeUtils;
import com.xieweifeng.service.UserService;
import com.xieweifeng.utils.MD5;
import com.xieweifeng.utils.UID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@Api(value = "授权中心")
public class MyTokenController {
    @ApiModelProperty(value = "redis工具类")
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;
    @RequestMapping("/getCode")
    @ApiOperation(value = "获取用于验证的code")
    public ResponseResult getCode(HttpServletRequest request, HttpServletResponse response){

       // Cookie[] cookies = request.getCookies();
        //创建一个响应类
        ResponseResult responseResult = ResponseResult.getResponseResult();
        //使用工具类获得指定长度的随机字符串
        String code = VerifyCodeUtils.generateVerifyCode(5);
        //将随机字符串添加到响应类
        responseResult.setResult(code);
        responseResult.setCode(200);
        responseResult.setSuccess("code获取成功");

        //使用工具类获得指定长度的随机字符串
        String uid ="UUID"+ UID.getUUID16();
        //将获得的两个字符串存入到redis中,用于在之后的登录中用于验证
        stringRedisTemplate.opsForValue().set(uid,code);
        //设置存入参数的过期时间,到时间后自动删除指定的key
        stringRedisTemplate.expire(uid,60, TimeUnit.SECONDS);

        System.out.println(uid);
        //根据随机生成的uuid,创建一个名称为authcode的cookie
        Cookie authcode = new Cookie("authcode", uid);
        //cookie作用的路径
        authcode.setPath("/");
        //cookie作用的域名
        authcode.setDomain("127.0.0.1");
        //将cookie添加到响应中
        response.addCookie(authcode);
        //返回获得的code

        return responseResult;
    }

    @RequestMapping("/login")
    public ResponseResult login(@RequestBody Map<String,Object> map) throws LoginException {


        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(map==null){
            throw new LoginException("用户名或密码错误");
        }

        String code = map.get("code").toString();
        String cookieKey = stringRedisTemplate.opsForValue().get(map.get("cookieKey").toString()).toString();
        if(!code.equals(cookieKey)){
            throw new LoginException("验证码已过期");
        }

        UserInfo loginName = userService.login(map.get("loginName").toString());

        if(loginName==null){
            throw new LoginException("用户名或密码错误");
        }
        Boolean checked = Boolean.valueOf(map.get("checked").toString());
        Object token = map.get("token");
        String s =null;
        if(token!=null){
            s = map.get("password").toString();
        }else {
            s = MD5.encryptPassword(map.get("password").toString(), "lcg");
        }

        if(!s.equals(loginName.getPassword())){
            throw new LoginException("用户名或密码错误");
        }
        responseResult.setCode(200);

        String s1 = JSON.toJSONString(loginName);
        String s2 = JWTUtils.generateToken(s1);

        responseResult.setToken(s2);

        stringRedisTemplate.opsForValue().set("USER"+loginName.getId(),s2);
        if(checked){
            stringRedisTemplate.expire("USER"+loginName.getId(),7,TimeUnit.DAYS);
        }else {
            stringRedisTemplate.expire("USER"+loginName.getId(),600,TimeUnit.SECONDS);
        }

        Map<String, String> authmap = loginName.getAuthmap();
        stringRedisTemplate.opsForHash().putAll("USERDATAAUTH"+loginName.getId(),authmap);
        if(checked){
            stringRedisTemplate.expire("USERDATAAUTH"+loginName.getId(),7,TimeUnit.DAYS);
        }else {
            stringRedisTemplate.expire("USERDATAAUTH"+loginName.getId(),600,TimeUnit.SECONDS);
        }

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String ym = simpleDateFormat1.format(new Date());
        String ymd = simpleDateFormat2.format(new Date());
        if(stringRedisTemplate.opsForHash().hasKey(ym,ymd)){
            stringRedisTemplate.opsForHash().increment(ym,ymd,1);
        }else {
            stringRedisTemplate.opsForHash().put(ym,ymd,"1");
        }

        Object[] values =  stringRedisTemplate.opsForHash().values(ym).toArray();
        Object[] keys =  stringRedisTemplate.opsForHash().keys(ym).toArray();
        loginName.setLoginKeys(keys);
        loginName.setLoginValues(values);
        loginName.setLoginCount(stringRedisTemplate.opsForHash().get(ym,ymd).toString());
        responseResult.setResult(loginName);
        return responseResult;
    }


    @RequestMapping("/exitUser")
    public ResponseResult exitUser(@RequestBody Map map){
        Object userId = map.get("userId");
        Boolean delete = null;
        Boolean delete1 = null;
        if(stringRedisTemplate.hasKey("USER" + userId.toString())){
            delete = stringRedisTemplate.delete("USER" + userId.toString());
        }
       if(stringRedisTemplate.hasKey("USERDATAAUTH" + userId.toString())){
           delete1 = stringRedisTemplate.delete("USERDATAAUTH" + userId.toString());
       }
        ResponseResult result = new ResponseResult();

        if(delete==true&&delete1==true){
            result.setCode(200);
            result.setSuccess("成功");
        }else if(delete==true||delete1==true){
            result.setCode(500);
            result.setSuccess("未完全成功");
        }else {
            result.setCode(500);
            result.setSuccess("完全失败");
        }
        return result;
    }
}
