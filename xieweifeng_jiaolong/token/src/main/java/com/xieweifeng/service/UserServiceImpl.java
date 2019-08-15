package com.xieweifeng.service;/*
@author 谢唯峰
@create 2019-08-06-9:34
*/

import com.xieweifeng.dao.UserDao;
import com.xieweifeng.entity.MenuInfo;
import com.xieweifeng.entity.RoleInfo;
import com.xieweifeng.entity.UserInfo;
import com.xieweifeng.exception.LoginException;
import com.xieweifeng.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;


    @Override
    public UserInfo login(String loginName) throws LoginException {
        UserInfo user = userDao.findUserByLoginNameEquals(loginName);
        if(user!=null){
            RoleInfo userId = userDao.findByUserId(user.getId());
            if(userId!=null){
                HashMap<String, String> authmap = new HashMap<>();
                List<MenuInfo> menuInfoByRoleId = userDao.findMenuInfoByRoleId(0L, user.getId());
                this.findMenu(0L,authmap,userId.getId());
                user.setAuthmap(authmap);
                user.setListMenuInfo(menuInfoByRoleId);
                return user;
            }
        }else {
            throw new LoginException("用户名或密码错误");
        }
        return null;
    }

    @Override
    public void findMenu(Long parentid, Map<String, String> map, Long roleId) {
        List<MenuInfo> menuInfoByRoleId = userDao.findMenuInfoByRoleId(parentid, roleId);
        for (MenuInfo m : menuInfoByRoleId) {
            this.findMenu(m.getId(), map, roleId);
            if(m.getLeval()!=1&&m.getUrl()!=null){
                map.put(m.getUrl(),"true");
            }
        }

    }

    @Override
    public UserInfo findUserTel(String tel) throws LoginException {
        UserInfo user = userDao.findUserTel(tel);
        if(user!=null){
            RoleInfo userId = userDao.findByUserId(user.getId());
            if(userId!=null){
                HashMap<String, String> authmap = new HashMap<>();
                List<MenuInfo> menuInfoByRoleId = userDao.findMenuInfoByRoleId(0L, user.getId());
                this.findMenu(0L,authmap,userId.getId());
                user.setAuthmap(authmap);
                user.setListMenuInfo(menuInfoByRoleId);
                return user;
            }
        }else {
            throw new LoginException("用户名或密码错误");
        }
        return null;

    }

    @Override
    public String getAuthcode(String tel) {
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        String appcode = "6fad691cc7b64a7885a0d8623f21ee33";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + "6fad691cc7b64a7885a0d8623f21ee33");
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", tel);
        String randomString = this.getRandomString();
        querys.put("param", "code:"+randomString);
        querys.put("tpl_id", "TP1711063");
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
           /* System.out.println("response.toString:"+response.toString());*/
            //获取response的body
            /*System.out.println("EntityUtils.toString:"+EntityUtils.toString(response.getEntity()));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return randomString;
    }


    public String getRandomString(){
        String result = "";
        for (int i = 0; i<6; i++) {
            int intVal = (int)(Math.random()*26+97);
            result = result +(char) intVal;
        }
        return result;
    }
}
