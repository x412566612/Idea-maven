package com.xieweifeng.web;/*
@author 谢唯峰
@create 2019-08-06-20:57
*/

import com.xieweifeng.entity.MenuInfo;
import com.xieweifeng.entity.ResponseResult;
import com.xieweifeng.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
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

@RestController
@Api(value = "菜单类,对权限进行CRUD操作")
public class MyMenuController {
    @Autowired
    private MenuService menuService;

    @Resource
    private RedisTemplate redisTemplate;


    @RequestMapping("findMenuInfoByParentIdAndRoleId")
    @ApiOperation(value = "分层查询指定角色的菜单")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为id的Long类型参数"
    )
    public ResponseResult findMenuInfoByParentIdAndRoleId(@RequestBody Map map){
        Long id = Long.valueOf(map.get("id").toString());
        ResponseResult responseResult = new ResponseResult();
        List<MenuInfo> all = menuService.findMenuInfoByParentIdAndRoleId(0L,id);
        responseResult.setResult(all);
        responseResult.setCode(200);
        responseResult.setSuccess("查询成功");
        return responseResult;
    }

    @RequestMapping("findMenuAll")
    @ApiOperation(value = "获取所有的菜单")
    public ResponseResult findMenuAll(){
        ResponseResult responseResult = new ResponseResult();
        List<MenuInfo> all = menuService.findAll(0L);
        responseResult.setResult(all);
        responseResult.setCode(200);
        responseResult.setSuccess("查询成功");
        return responseResult;
    }
    @RequestMapping("findMenuByParentId")
    @ApiOperation(value = "获取菜单的4级子菜单")
    @ApiImplicitParam(
            name = "parentIds",
            value = "传入一组Long集合"
    )
    public ResponseResult findMenuByParentId(@RequestBody Long[] parentIds){
        ResponseResult responseResult = new ResponseResult();
        List<MenuInfo> all = menuService.findMenuByParentId(parentIds);
        responseResult.setResult(all);
        responseResult.setCode(200);
        responseResult.setSuccess("查询成功");
        return responseResult;
    }
    @RequestMapping("findRoleByRoleid")
    @ApiOperation(value = "获取指定角色的所有菜单ID")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为id的Long类型参数"
    )
    public ResponseResult findRoleByRoleid(@RequestBody Map map){
        Long id = Long.valueOf(map.get("id").toString());
        ResponseResult responseResult = new ResponseResult();
        List<MenuInfo> all = menuService.findRoleByRoleid(id);
        ArrayList<Long> objects = new ArrayList<>();
        all.forEach(m->{
            objects.add(m.getId());
        });
        responseResult.setResult(objects);
        responseResult.setCode(200);
        responseResult.setSuccess("查询成功");
        return responseResult;
    }

    @RequestMapping("insertMenu")
    @ApiOperation(value = "添加菜单")
    @ApiImplicitParam(
            name = "menuInfo",
            value = "传入一个菜单对象,用于做添加操作"
    )
    public ResponseResult insertMenu(@RequestBody MenuInfo menuInfo){
        ResponseResult responseResult = new ResponseResult();
        Integer integer = menuService.insertMenu(menuInfo);
        responseResult.setCode(200);
        responseResult.setSuccess("添加成功");
        return responseResult;
    }
    @RequestMapping("updateMenu")
    @ApiOperation(value = "修改菜单")
    @ApiImplicitParam(
            name = "menuInfo",
            value = "传入一个菜单对象,用于做修改操作"
    )
    public ResponseResult updateMenu(@RequestBody MenuInfo menuInfo){
        ResponseResult responseResult = new ResponseResult();
        Integer integer = menuService.updateMenu(menuInfo);
        responseResult.setCode(200);
        responseResult.setSuccess("修改成功");
        return responseResult;
    }
    @RequestMapping("deleteMenu")
    @ApiOperation(value = "删除菜单")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为id的Long类型参数"
    )
    public ResponseResult deleteMenu(@RequestBody Map map){
        Long id = Long.valueOf(map.get("id").toString());
        ResponseResult responseResult = new ResponseResult();
        Integer integer = menuService.deleteMenu(id);
        responseResult.setCode(200);
        responseResult.setSuccess("删除成功");
        return responseResult;
    }
}
