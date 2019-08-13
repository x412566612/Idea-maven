package com.xieweifeng.service;/*
@author 谢唯峰
@create 2019-08-07-14:06
*/

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xieweifeng.dao.UserInfoDao;
import com.xieweifeng.entity.Condition;
import com.xieweifeng.entity.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    @ApiOperation("根据用户名和时间还有性别查询用户")
    public PageInfo<UserInfo> findByNameAndDateAndSex(Condition condition) {
        PageHelper.startPage(condition.getPageNum(),condition.getPageSize());
        List<UserInfo> userInfos = userInfoDao.
                findByNameAndDateAndSex(
                        condition.getUserName(),
                        condition.getStr(),
                        condition.getEnd(),
                        condition.getSexs());
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfos);
        return pageInfo;
    }

    @Override
    @ApiOperation("根据ID查询用户")
    public UserInfo findUserInfoById(Long id) {
        UserInfo userInfoById = userInfoDao.findUserInfoById(id);
        return userInfoById;
    }

    @Override
    public UserInfo findUserInfoByLoginName(String loginName) {
        UserInfo userInfoByLoginName = userInfoDao.findUserInfoByLoginName(loginName);
        return userInfoByLoginName;
    }

    @Override
    public List<UserInfo> findUserByRoleId(Long id) {
        List<UserInfo> userByRoleId = userInfoDao.findUserByRoleId(id);
        return userByRoleId;
    }

    @Override
    @Transactional
    @ApiOperation("添加用户")
    public Integer insertUserInfo(UserInfo userInfo) {
        Integer integer = userInfoDao.insertUserInfo(userInfo);
        return integer;
    }

    @Override
    @Transactional
    @ApiOperation("修改用户")
    public Integer updateUserInfo(UserInfo userInfo) {
        Integer integer = userInfoDao.updateUserInfo(userInfo);
        return integer;
    }

    @Override
    @Transactional
    @ApiOperation("批量删除用户")
    public Integer deleteUserInfo(Long[] ids) {
        for (int i=0;i<ids.length;i++){
            userInfoDao.deleteUserAndRoles(ids[i]);
        }
        Integer integer = userInfoDao.deleteUserInfo(ids);
        return integer;
    }

    @Override
    @ApiOperation("添加用户关联的角色")
    public Integer insertUserAndRoles(Long userId, Long roleId) {
        userInfoDao.deleteUserAndRoles(userId);
        Integer integer = userInfoDao.insertUserAndRoles(userId, roleId);
        return integer;
    }
}
