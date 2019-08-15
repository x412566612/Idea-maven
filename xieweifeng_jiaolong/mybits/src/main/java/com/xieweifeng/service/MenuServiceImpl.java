package com.xieweifeng.service;/*
@author 谢唯峰
@create 2019-08-06-20:50
*/

import com.xieweifeng.dao.MenuDao;
import com.xieweifeng.entity.MenuInfo;
import com.xieweifeng.utils.TwitterIdWorker;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 菜单service层
 */
import java.util.Date;
import java.util.List;
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    @Override
    @ApiOperation(value = "分层查询指定角色的菜单")
    public List<MenuInfo> findMenuInfoByParentIdAndRoleId(Long parentId, Long roleId) {
        List<MenuInfo> info = menuDao.findMenuInfoByParentIdAndRoleId(parentId,roleId);
        info.forEach(m->{
            List<MenuInfo> all = this.findMenuInfoByParentIdAndRoleId(m.getId(),roleId);
            m.setMenuInfoList(all);
        });
        return info;
    }

    @Override
    @ApiOperation(value = "通过父菜单递归,将所有菜单分层")
    public List<MenuInfo> findAll(Long parentId) {
        List<MenuInfo> info = menuDao.findMenuInfoAll(parentId);
        info.forEach(m->{
            List<MenuInfo> all = this.findAll(m.getId());
            m.setMenuInfoList(all);
        });
        return info;
    }


    @Override
    @ApiOperation(value = "查询指定菜单的4级子菜单")
    public List<MenuInfo> findMenuByParentId(Long[] parentIds) {
        List<MenuInfo> parentId = menuDao.findMenuByParentId(parentIds);
        return parentId;
    }


    @Override
    @ApiOperation(value = "查询指定角色的菜单")
    public List<MenuInfo> findRoleByRoleid(Long id) {
        List<MenuInfo> roleByRoleid = menuDao.findRoleByRoleid(id);
        return roleByRoleid;
    }

    @Override
    @ApiOperation(value = "添加菜单")
    public Integer insertMenu(MenuInfo menuInfo) {
        TwitterIdWorker twitterIdWorker = new TwitterIdWorker();
        menuInfo.setId(twitterIdWorker.nextId());
        menuInfo.setCreateTime(new Date());
        menuInfo.setUpdateTime(new Date());
        menuInfo.setVersion(1L);
        Integer integer = menuDao.insertMenu(menuInfo);
        return integer;
    }

    @Override
    @ApiOperation(value = "修改菜单")
    public Integer updateMenu(MenuInfo menuInfo) {
        menuInfo.setUpdateTime(new Date());
        menuInfo.setVersion(menuInfo.getVersion()+1);
        Integer integer = menuDao.updateMenu(menuInfo);
        return integer;
    }

    @Override
    @ApiOperation(value = "删除菜单")
    public Integer deleteMenu(Long id) {
        menuDao.deleteMenuByParentId(id);
        Integer integer = menuDao.deleteMenu(id);
        return integer;
    }
}
