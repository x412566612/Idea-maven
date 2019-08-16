package com.xieweifeng.service;/*
@author 谢唯峰
@create 2019-08-08-16:52
*/

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xieweifeng.dao.RoleInfoDao;
import com.xieweifeng.entity.RoleInfo;
import com.xieweifeng.utils.TwitterIdWorker;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service
public class RoleInfoServiceImpl implements RoleInfoService {
    @Autowired
    private RoleInfoDao roleInfoDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @ApiOperation("根据角色名称,模糊查询角色信息")
    @Override
    public List<RoleInfo> findByRoleAll( String query) {
        List<RoleInfo> byRoleAll = roleInfoDao.findByRoleAll(query);
        return byRoleAll;
    }

    @Override
    @ApiOperation("根据角色名称,查询所拥有的子角色")
    public List<RoleInfo> findRoleByParentId(Long parentId,List<RoleInfo> infoList) {
        List<RoleInfo> roleByParentId = roleInfoDao.findRoleByParentId(parentId);
        roleByParentId.forEach(r->{
            List<RoleInfo> role = this.findRoleByParentId(r.getId(),infoList);
        });
        infoList.addAll(roleByParentId);
        return infoList;
    }

    @Override
    public RoleInfo findRoleById(Long id) {
        return roleInfoDao.findRoleById(id);
    }


    @Override
    @ApiOperation("根据角色名称,模糊查询角色信息,并关联查询角色绑定用户和权限")
    public PageInfo<RoleInfo> findRoleByLikeNameAll(String roleName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<RoleInfo> roleByLikeNameAll = roleInfoDao.findRoleByLikeNameAll(roleName);
        PageInfo<RoleInfo> roleInfoPageInfo = new PageInfo<>(roleByLikeNameAll);
        return roleInfoPageInfo;
    }

    @Override
    @ApiOperation("添加角色")
    public Integer insertRole(RoleInfo roleInfo) {
        TwitterIdWorker twitterIdWorker = new TwitterIdWorker();
        roleInfo.setVersion(1L);
        roleInfo.setId(twitterIdWorker.nextId());
        roleInfo.setCreateTime(new Date());
        roleInfo.setUpdateTime(new Date());
        Integer integer = roleInfoDao.insertRole(roleInfo);
        roleInfoDao.insertRoleAndMenu(roleInfo.getId(), roleInfo.getMenuIds());
        return integer;
    }

    @Override
    @ApiOperation("修改角色,及角色所绑定的菜单")
    public Integer updateRole(RoleInfo roleInfo) {
        Long id = roleInfo.getId();
        roleInfo.setVersion(roleInfo.getVersion()+1);
        roleInfo.setUpdateTime(new Date());
        roleInfoDao.updateRole(roleInfo);
        roleInfoDao.deleteRoleAndMenu(roleInfo.getId());
        if(stringRedisTemplate.hasKey("USER" + id)){
            stringRedisTemplate.delete("USER" + id);
        }
        if(stringRedisTemplate.hasKey("USERDATAAUTH" + id)){
            stringRedisTemplate.delete("USERDATAAUTH" + id);
        }
        Integer integer = roleInfoDao.insertRoleAndMenu(roleInfo.getId(), roleInfo.getMenuIds());
        return integer;
    }

    @Override
    @ApiOperation("给角色绑定菜单")
    public Integer insertRoleAndMenu(Long roleId, Long[] menuIds) {
        Integer integer = roleInfoDao.insertRoleAndMenu(roleId, menuIds);
        return integer;
    }

    @Override
    @ApiOperation("删除角色及角色绑定的菜单")
    public Integer deleteRole(Long roleId) {
        roleInfoDao.deleteRoleAndMenu(roleId);
        Integer integer = roleInfoDao.deleteRole(roleId);
        if(stringRedisTemplate.hasKey("USER" + roleId)){
            stringRedisTemplate.delete("USER" + roleId);
        }
        if(stringRedisTemplate.hasKey("USERDATAAUTH" + roleId)){
            stringRedisTemplate.delete("USERDATAAUTH" + roleId);
        }
        return integer;
    }

    @Override
    public Integer clearRoleCorrelationAll(Long id) {

       Integer integer =  roleInfoDao.clearRoleCorrelationUser(id);

       Integer integer1 =  roleInfoDao.clearRoleCorrelationMenu(id);
        return integer;
    }

}
