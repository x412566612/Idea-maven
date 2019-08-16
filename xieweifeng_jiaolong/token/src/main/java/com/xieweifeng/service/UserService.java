package com.xieweifeng.service;/*
@author 谢唯峰
@create 2019-08-06-9:35
*/

import com.xieweifeng.entity.MenuInfo;
import com.xieweifeng.entity.UserInfo;
import com.xieweifeng.exception.LoginException;

import java.util.Map;

public interface UserService {
    public UserInfo login(String loginName) throws LoginException;
    public void findMenu(Long parentid, Map<String,String> map,Long roleId);

    public UserInfo findUserTel(String tel) throws LoginException;
    public String getAuthcode(String tel);
    public String getRandomString();

    UserInfo findUserByLoginNameAndEmail(String loginName, String email);

    Integer updateUserPassword(Long id, String password);
}
