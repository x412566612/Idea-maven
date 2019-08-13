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
    @ApiOperation(value = "通过父菜单递归,将所有菜单分层")
    public List<MenuInfo> findAll(Long parentId) {
        List<MenuInfo> info = menuDao.findMenuInfoByParentIdEquals(parentId);
        info.forEach(m->{
            List<MenuInfo> all = this.findAll(m.getId());
            m.setMenuInfoList(all);
        });
        return info;
    }

    @Override
    @ApiOperation(value = "通过父菜单递归,将所有菜单分层,不获取最后一级菜单,当循环到最后一级菜单时,结束循环")
    public List<MenuInfo> findMenuBySan(Long parentId) {
        List<MenuInfo> info = menuDao.findMenuInfoByParentIdEquals(parentId);
        if(info.size()==0||info.get(0).getLeval()>3){
            return null;
        }
        info.forEach(m->{
                List<MenuInfo> all = this.findMenuBySan(m.getId());
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
