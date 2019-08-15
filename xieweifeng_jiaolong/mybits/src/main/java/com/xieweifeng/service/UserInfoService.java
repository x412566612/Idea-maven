package com.xieweifeng.service;/*
@author 谢唯峰
@create 2019-08-07-14:03
*/

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.xieweifeng.entity.Condition;
import com.xieweifeng.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;

public interface UserInfoService {
    public PageInfo<UserInfo> findByNameAndDateAndSex(Condition condition);
    public UserInfo findUserInfoById(Long id);
    public UserInfo findUserInfoByLoginName(String loginName);
    public List<UserInfo> findUserByRoleId( Long id);
    public Integer insertUserInfo(UserInfo userInfo);
    public Integer updateUserInfo(UserInfo userInfo);
    public Integer deleteUserInfo(Long id,Integer status);
    public Integer insertUserAndRoles(Long userId,Long roleId);
    public  void createExcel() throws IOException;


}
