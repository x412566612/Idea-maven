package com.xieweifeng.service;/*
@author 谢唯峰
@create 2019-08-06-20:49
*/

import com.xieweifeng.entity.MenuInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuService {
    public List<MenuInfo> findMenuInfoByParentIdAndRoleId(Long parentId,Long roleId);
    public List<MenuInfo> findAll(Long parentId);
    public List<MenuInfo> findMenuByParentId(Long[] parentIds);

    public List<MenuInfo> findRoleByRoleid(Long id);

    public Integer insertMenu(MenuInfo menuInfo);
    public Integer updateMenu(MenuInfo menuInfo);
    public Integer deleteMenu(Long id);

}
