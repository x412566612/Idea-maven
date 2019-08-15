package com.xieweifeng.dao;/*
@author 谢唯峰
@create 2019-08-07-14:43
*/

import com.xieweifeng.entity.MenuInfo;
import com.xieweifeng.entity.RoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuDao {
    public List<MenuInfo> findMenuInfoByParentIdAndRoleId(@Param("parentId") Long parentId,@Param("roleId") Long roleId);
    public List<MenuInfo> findMenuInfoAll(@Param("parentId") Long parentId);
    public List<MenuInfo> findRoleByRoleid(@Param("id")Long id);

    public List<MenuInfo> findMenuByParentId(@Param("parentIds")Long[] parentIds);
    public Integer insertMenu(MenuInfo menuInfo);
    public Integer updateMenu(MenuInfo menuInfo);
    public Integer deleteMenu(@Param("id")Long id);
    public Integer deleteMenuByParentId(@Param("parentId")Long parentId);
}
