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
}