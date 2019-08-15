package com.xieweifeng.dao;/*
@author 谢唯峰
@create 2019-08-05-21:13
*/

import com.xieweifeng.entity.MenuInfo;
import com.xieweifeng.entity.RoleInfo;
import com.xieweifeng.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao  {
    public UserInfo findUserByLoginNameEquals(@Param("loginName") String loginName);
    public RoleInfo findByUserId(@Param("id") Long id);
    public List<MenuInfo> findMenuInfoByRoleId(@Param("prentid") Long prentid,@Param("roleID")  Long roleID);

    public  UserInfo findUserTel(@Param("tel")String tel);
}
