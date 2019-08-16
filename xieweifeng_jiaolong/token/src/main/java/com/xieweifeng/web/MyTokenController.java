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
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
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


    @RequestMapping("/getEmail")
    @ApiOperation(value = "向指定邮箱发送一条验证码")
    public  ResponseResult getEmail(@RequestBody Map map) throws EmailException {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        String myemail = map.get("email").toString();
        HtmlEmail email=new HtmlEmail();
        try {
            email.setHostName("smtp.163.com");//邮箱的SMTP服务器，一般123邮箱的是smtp.123.com,qq邮箱为smtp.qq.com
            email.setCharset("utf-8");//设置发送的字符类型
            email.setFrom("x18580523803@163.com","谢先生");//发送人的邮箱为自己的，用户名可以随便填
            email.setAuthentication("x18580523803@163.com", "xieweifeng1990");
            //此处填写邮箱地址和客户端授权码
            email.addTo(myemail);//设置收件人
            email.setSubject("重置登陆账户用户密码");//设置发送主题
            String randomCode = userService.getRandomString();
            email.setMsg("验证码:"+randomCode);//设置发送内容
            email.send();//进行发送
            stringRedisTemplate.opsForValue().set(myemail,randomCode);
            stringRedisTemplate.expire(myemail,600,TimeUnit.SECONDS);
            responseResult.setCode(200);
            responseResult.setSuccess("验证码发送成功");
            responseResult.setResult(randomCode);
        }catch (Exception e){
            responseResult.setCode(500);
            responseResult.setError("邮箱不存在");
        }finally {
            return responseResult;
        }
    }

    @RequestMapping("/resetUserPassword")
    @ApiOperation(value = "根据邮箱,重置指定账户密码")
    public ResponseResult resetUserPassword(@RequestBody Map map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        String email = map.get("email").toString();
        String loginName = map.get("loginName").toString();
        String code = map.get("code").toString();
        String password = MD5.encryptPassword(map.get("password").toString(), "lcg");
        if(stringRedisTemplate.opsForValue().get(email).equals(code)){
          UserInfo userInfo = userService.findUserByLoginNameAndEmail(loginName,email);
          if(userInfo!=null){
             Integer integer = userService.updateUserPassword(userInfo.getId(),password);
              responseResult.setCode(200);
              responseResult.setSuccess("密码重置成功,请重新登录");
          }else {
              responseResult.setCode(500);
              responseResult.setSuccess("绑定邮箱有误,请重新输入");
          }
        }else {
            responseResult.setCode(500);
            responseResult.setSuccess("验证码有误,请重新输入");
        }
        return responseResult;
    }
    @RequestMapping("/getAuthcode")
    @ApiOperation(value = "获取发送一条手机验证码")
    public ResponseResult getAuthcode(@RequestBody Map map, HttpServletResponse response){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        String tel = map.get("tel").toString();
        if(tel!=null&&tel!=""){
            String authcode = userService.getAuthcode(tel);
            stringRedisTemplate.opsForValue().set(tel,authcode);
            stringRedisTemplate.expire(tel,6000,TimeUnit.SECONDS);
            System.out.println(authcode);
            responseResult.setCode(200);
            responseResult.setSuccess("验证码已发送,请在60秒内完成登录");
            responseResult = this.setCode(responseResult, response);
        }else {
            responseResult.setCode(500);
            responseResult.setError("手机号不合法");
        }
        return responseResult;
    }
    @RequestMapping("/getCode")
    @ApiOperation(value = "获取用于验证的code")
    public ResponseResult getCode( HttpServletResponse response){
       // Cookie[] cookies = request.getCookies();
        //创建一个响应类
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        responseResult.setSuccess("code获取成功");
        //返回获得的code
        responseResult = this.setCode(responseResult, response);
        return responseResult;
    }


    @RequestMapping("/loginPhone")
    @ApiOperation(value = "用于手机登录,map必须携带手机号码,手机验证码,可携带是否7日免登录状态")
    public ResponseResult  loginPhone(@RequestBody Map<String,Object> map) throws LoginException {
       /* phone:{tel:"",authcode:"",checked:true}*/
        String tel = map.get("tel").toString();
        String authcode = map.get("authcode").toString();

        if(tel==null||tel==""||!stringRedisTemplate.hasKey(tel)){
            throw new LoginException("手机或验证码错误");
        }
        String s = stringRedisTemplate.opsForValue().get(tel);
        if(s==null||s.equals("")||!s.equals(authcode)){
            throw new LoginException("手机或验证码错误");
        }
        ResponseResult responseResult = ResponseResult.getResponseResult();
        Boolean checked = Boolean.valueOf(map.get("checked").toString());

        UserInfo loginName = userService.findUserTel(tel);
        responseResult= this.setLogin(loginName,responseResult,checked);
        return responseResult;
    }
    @RequestMapping("/login")
    public ResponseResult login(@RequestBody Map<String,Object> map) throws LoginException {

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
        ResponseResult responseResult = ResponseResult.getResponseResult();
        Boolean checked = Boolean.valueOf(map.get("checked").toString());
        responseResult= this.setLogin(loginName,responseResult,checked);
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


    public ResponseResult setCode(ResponseResult responseResult, HttpServletResponse response){
        //使用工具类获得指定长度的随机字符串
        String code = VerifyCodeUtils.generateVerifyCode(5);
        //将随机字符串添加到响应类
        responseResult.setResult(code);
        //使用工具类获得指定长度的随机字符串
        String uid ="UUID"+ UID.getUUID16();
        //将获得的两个字符串存入到redis中,用于在之后的登录中用于验证
        stringRedisTemplate.opsForValue().set(uid,code);
        //设置存入参数的过期时间,到时间后自动删除指定的key
        stringRedisTemplate.expire(uid,60, TimeUnit.SECONDS);


        //根据随机生成的uuid,创建一个名称为authcode的cookie
        Cookie authcode = new Cookie("authcode", uid);
        //cookie作用的路径
        authcode.setPath("/");
        //cookie作用的域名
        authcode.setDomain("127.0.0.1");
        //将cookie添加到响应中
        response.addCookie(authcode);
        return  responseResult;
    }

    public ResponseResult setLogin(UserInfo userInfo,ResponseResult responseResult,Boolean checked){
        String s1 = JSON.toJSONString(userInfo);
        String s2 = JWTUtils.generateToken(s1);
        responseResult.setCode(200);
        responseResult.setToken(s2);
        //判断是否免登陆
        stringRedisTemplate.opsForValue().set("USER"+userInfo.getId(),s2);
        if(checked){
            stringRedisTemplate.expire("USER"+userInfo.getId(),7,TimeUnit.DAYS);
        }else {
            stringRedisTemplate.expire("USER"+userInfo.getId(),600,TimeUnit.SECONDS);
        }
        Map<String, String> authmap = userInfo.getAuthmap();
        stringRedisTemplate.opsForHash().putAll("USERDATAAUTH"+userInfo.getId(),authmap);
        if(checked){
            stringRedisTemplate.expire("USERDATAAUTH"+userInfo.getId(),7,TimeUnit.DAYS);
        }else {
            stringRedisTemplate.expire("USERDATAAUTH"+userInfo.getId(),600,TimeUnit.SECONDS);
        }
        //是否免登陆结束

        //开始存值
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String ymd = simpleDateFormat1.format(new Date());

        if(!stringRedisTemplate.hasKey("loginThirtyDays")){
            stringRedisTemplate.opsForList().rightPush("loginThirtyDays",ymd);
        }
        Long size = stringRedisTemplate.opsForList().size("loginThirtyDays");
        String s = stringRedisTemplate.opsForList().index("loginThirtyDays",size-1);
        if(!s.equals(ymd)&&size==30){
            stringRedisTemplate.opsForList().leftPop("loginThirtyDays");
            stringRedisTemplate.opsForList().rightPush("loginThirtyDays",ymd);
        }
        stringRedisTemplate.opsForSet().add(ymd,userInfo.getId().toString());
        //存值结束

        //开始取值
        List<String> values = new ArrayList<>();
        List<String> keys = stringRedisTemplate.opsForList().range("loginThirtyDays",0,-1);
        keys.forEach(k->{
            values.add(stringRedisTemplate.opsForSet().size(k).toString());
        });
        userInfo.setLoginKeys(keys.toArray());
        userInfo.setLoginValues(values.toArray());
        //取值结束
        responseResult.setResult(userInfo);
        return responseResult;
    }

}
