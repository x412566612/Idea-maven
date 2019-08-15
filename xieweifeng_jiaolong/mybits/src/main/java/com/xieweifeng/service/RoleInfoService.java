package com.xieweifeng.service;/*
@author 谢唯峰
@create 2019-08-08-16:51
*/

import com.github.pagehelper.PageInfo;
import com.xieweifeng.entity.RoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleInfoService {
    public List<RoleInfo> findByRoleAll( String query);
    public List<RoleInfo> findRoleByParentId(Long parentId,List<RoleInfo> infoList);
    public RoleInfo findRoleById(Long id);
    public PageInfo<RoleInfo> findRoleByLikeNameAll(String roleName ,Integer pageNum,Integer pageSize);
    public Integer insertRole(RoleInfo roleInfo);
    public Integer updateRole(RoleInfo roleInfo);

    public Integer insertRoleAndMenu( Long roleId,Long[] menuIds);

    public Integer deleteRole( Long roleId);

    public Integer clearRoleCorrelationAll(Long id);
}
