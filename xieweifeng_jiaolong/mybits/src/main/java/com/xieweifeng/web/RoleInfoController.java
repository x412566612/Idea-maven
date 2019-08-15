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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为query的字符串"
    )
    public ResponseResult findRoleInfoAll(@RequestBody Map map){
        ResponseResult responseResult = new ResponseResult();
        String query = map.get("query").toString();
        List<RoleInfo> all = roleInfoService.findByRoleAll(query);
        responseResult.setResult(all);
        responseResult.setCode(200);
        return responseResult;
    }

    @ApiOperation("根据角色ID,查询所拥有的子角色")
    @RequestMapping("findRoleByParentId")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为query的字符串"
    )
    public ResponseResult findRoleByParentId(@RequestBody Map map) {
        Long parentId = Long.valueOf(map.get("query").toString());
        ResponseResult responseResult = new ResponseResult();
        List<RoleInfo> list = new ArrayList<>();
        List<RoleInfo> roleByParentId = roleInfoService.findRoleByParentId(parentId, list);
        responseResult.setCode(200);
        responseResult.setResult(roleByParentId);
        return responseResult;
    }
    @ApiOperation("根据角色ID,查询包括自己在内的子角色")
    @RequestMapping("findRoleById")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为query的字符串"
    )
    public ResponseResult findRoleById(@RequestBody Map map) {
        Long parentId = Long.valueOf(map.get("query").toString());
        ResponseResult responseResult = new ResponseResult();
        List<RoleInfo> list = new ArrayList<>();
        RoleInfo roleInfo = roleInfoService.findRoleById(parentId);
        list.add(roleInfo);
        List<RoleInfo> roleByParentId = roleInfoService.findRoleByParentId(parentId, list);
        responseResult.setCode(200);
        responseResult.setResult(roleByParentId);
        return responseResult;
    }


    @ApiOperation("根据角色名称,模糊查询角色信息,并关联查询角色绑定用户和权限")
    @RequestMapping("findRoleByLikeNameAll")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带三个参数," +
                    "名称为roleName的字符串,"+"名称为pageNum的Integer参数,"+",名称为pageSize的Integer参数"
    )
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
    @ApiImplicitParam(
            name = "roleInfo",
            value = "传入一个角色对象,用于做添加操作"
    )
    public ResponseResult insertRole(@RequestBody RoleInfo roleInfo){
        ResponseResult responseResult = new ResponseResult();
        Integer integer = roleInfoService.insertRole(roleInfo);
        responseResult.setCode(200);
        responseResult.setSuccess("添加成功");
        return responseResult;
    }

    @ApiOperation("添加角色关联菜单")
    @RequestMapping("insertRoleAndMenu")
    @ApiImplicitParam(
            name = "roleInfo",
            value = "传入一个角色对象,用于做角色的关联操作"
    )
    public ResponseResult insertRoleAndMenu(@RequestBody RoleInfo roleInfo){
        ResponseResult responseResult = new ResponseResult();
        Long parentId = roleInfo.getParentId();
        RoleInfo roleById = roleInfoService.findRoleById(parentId);
        roleInfo.setLeval(roleById.getLeval()+1);
        roleInfoService.insertRoleAndMenu(roleInfo.getId(),roleInfo.getMenuIds());
        responseResult.setCode(200);
        responseResult.setSuccess("关联成功");
        return responseResult;
    }

    @ApiOperation("删除角色")
    @RequestMapping("deleteRole")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为id的Integer参数"
    )
    public ResponseResult deleteRole(@RequestBody Map map){
        ResponseResult responseResult = new ResponseResult();
        Integer id = roleInfoService.deleteRole(Long.valueOf(map.get("id").toString()));
        responseResult.setCode(200);
        responseResult.setSuccess("删除成功");
        return responseResult;
    }
    @ApiOperation("修改角色")
    @RequestMapping("updateRole")
    @ApiImplicitParam(
            name = "roleInfo",
            value = "传入一个角色对象,用于做修改操作"
    )
    public ResponseResult updateRole(@RequestBody RoleInfo roleInfo){
        ResponseResult responseResult = new ResponseResult();
        Long parentId = roleInfo.getParentId();
        try {
            Long parentId1 = roleInfo.getParentId();
            if(parentId1!=0){
                RoleInfo roleById = roleInfoService.findRoleById(parentId);
                roleInfo.setLeval(roleById.getLeval()+1);
            }
        }finally {
            roleInfoService.updateRole(roleInfo);
            responseResult.setCode(200);
            responseResult.setSuccess("修改成功");
            return responseResult;
        }

    }
    @ApiOperation("解除角色绑定的所有用户和权限")
    @RequestMapping("clearRoleCorrelationAll")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为id的Integer参数"
    )
    public ResponseResult clearRoleCorrelationAll(@RequestBody Map map){
        ResponseResult responseResult = new ResponseResult();
        Integer roleId = roleInfoService.clearRoleCorrelationAll(Long.valueOf(map.get("id").toString()));
        responseResult.setCode(200);
        responseResult.setSuccess("清理成功");
        return responseResult;
    }
}
