package com.xieweifeng.dao;/*
@author 谢唯峰
@create 2019-08-08-16:50
*/

import com.xieweifeng.entity.RoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleInfoDao {
    public List<RoleInfo> findByRoleAll(@Param("query") String query);

    public List<RoleInfo> findRoleByLikeNameAll(@Param("roleName") String roleName );


    public Integer insertRole(RoleInfo roleInfo);

    public Integer updateRole(RoleInfo roleInfo);

    public Integer insertRoleAndMenu(@Param("roleId") Long roleId,@Param("menuIds") Integer[] menuIds);

    public Integer deleteRole(@Param("roleId") Long roleId);

    public Integer deleteRoleAndMenu(@Param("roleId") Long roleId);
}
