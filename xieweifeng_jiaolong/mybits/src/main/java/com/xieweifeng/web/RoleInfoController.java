package com.xieweifeng.web;/*
@author 谢唯峰
@create 2019-08-06-20:57
*/

import com.github.pagehelper.PageInfo;
import com.xieweifeng.entity.MenuInfo;
import com.xieweifeng.entity.ResponseResult;
import com.xieweifeng.entity.RoleInfo;
import com.xieweifeng.service.MenuService;
import com.xieweifeng.service.RoleInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 角色类
 */
@RestController
@Api(value = "角色类,对角色进程CRUD操作")
public class RoleInfoController {
    @Autowired
    private RoleInfoService roleInfoService;


    @ApiOperation("根据角色名称,模糊查询角色信息")
    @RequestMapping("findRoleInfoAll")
    public ResponseResult findRoleInfoAll(@RequestBody Map map){
        ResponseResult responseResult = new ResponseResult();
        String query = map.get("query").toString();
        List<RoleInfo> all = roleInfoService.findByRoleAll(query);
        responseResult.setResult(all);
        responseResult.setCode(200);
        return responseResult;
    }
    @ApiOperation("根据角色名称,模糊查询角色信息,并关联查询角色绑定用户和权限")
    @RequestMapping("findRoleByLikeNameAll")
    public ResponseResult findRoleByLikeNameAll(@RequestBody Map map){
        ResponseResult responseResult = new ResponseResult();
        String roleName = map.get("roleName").toString();
        Integer pageNum = Integer.valueOf(map.get("pageNum").toString()) ;
        Integer pageSize = Integer.valueOf(map.get("pageSize").toString()) ;
        PageInfo<RoleInfo> role = roleInfoService.findRoleByLikeNameAll(roleName, pageNum, pageSize);
        responseResult.setCode(200);
        responseResult.setResult(role);
        return responseResult;
    }
    @ApiOperation("添加角色")
    @RequestMapping("insertRole")
    public ResponseResult insertRole(@RequestBody RoleInfo roleInfo){
        ResponseResult responseResult = new ResponseResult();
        Integer integer = roleInfoService.insertRole(roleInfo);
        responseResult.setCode(200);
        responseResult.setSuccess("添加成功");
        return responseResult;
    }

    @ApiOperation("添加角色关联菜单")
    @RequestMapping("insertRoleAndMenu")
    public ResponseResult insertRoleAndMenu(@RequestBody RoleInfo roleInfo){
        ResponseResult responseResult = new ResponseResult();
        roleInfoService.insertRoleAndMenu(roleInfo.getId(),roleInfo.getMenuIds());
        responseResult.setCode(200);
        responseResult.setSuccess("关联成功");
        return responseResult;
    }

    @ApiOperation("删除角色")
    @RequestMapping("deleteRole")
    public ResponseResult deleteRole(@RequestBody Map map){
        ResponseResult responseResult = new ResponseResult();
        Integer id = roleInfoService.deleteRole(Long.valueOf(map.get("id").toString()));
        responseResult.setCode(200);
        responseResult.setSuccess("删除成功");
        return responseResult;
    }
    @ApiOperation("修改角色")
    @RequestMapping("updateRole")
    public ResponseResult updateRole(@RequestBody RoleInfo roleInfo){
        ResponseResult responseResult = new ResponseResult();
        Integer roleId = roleInfoService.updateRole(roleInfo);
        responseResult.setCode(200);
        responseResult.setSuccess("修改成功");
        return responseResult;
    }
}
