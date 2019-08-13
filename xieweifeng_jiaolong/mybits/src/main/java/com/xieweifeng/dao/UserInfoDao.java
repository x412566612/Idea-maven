package com.xieweifeng.dao;/*
@author 谢唯峰
@create 2019-08-07-14:06
*/

import com.github.pagehelper.Page;
import com.xieweifeng.entity.Condition;
import com.xieweifeng.entity.RoleInfo;
import com.xieweifeng.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserInfoDao {
    public List<UserInfo> findByNameAndDateAndSex(@Param("userName") String userName,@Param("str") Date str,@Param("end")Date end,@Param("sex")String sex);

    public UserInfo findUserInfoById(@Param("id") Long id);
    public UserInfo findUserInfoByLoginName(@Param("loginName") String loginName);
    public List<UserInfo> findUserByRoleId(@Param("id") Long id);
    public Integer insertUserInfo(UserInfo userInfo);
    public Integer updateUserInfo(UserInfo userInfo);
    public Integer deleteUserInfo(@Param("ids") Long[] ids);
    public Integer insertUserAndRoles(@Param("userId")Long userId,@Param("roleId")Long roleIds);
    public Integer deleteUserAndRoles(@Param("userId")Long userId);
}
