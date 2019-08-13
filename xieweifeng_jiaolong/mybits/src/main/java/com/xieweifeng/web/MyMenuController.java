package com.xieweifeng.web;/*
@author 谢唯峰
@create 2019-08-06-20:57
*/

import com.xieweifeng.entity.MenuInfo;
import com.xieweifeng.entity.ResponseResult;
import com.xieweifeng.service.MenuService;
import io.swagger.annotations.Api;
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


    @RequestMapping("findMenuAll")
    @ApiOperation(value = "获取所有的菜单,并将获取到的菜单,存入redis缓存")

    public ResponseResult findMenuAll(){
        ResponseResult responseResult = new ResponseResult();
        //获取redis缓存中的参数
        List<MenuInfo> findMenuAll = redisTemplate.opsForList().range("findMenuAll", 0, -1);
        //redis缓存不为空,则返回缓存中的参数
        if(findMenuAll!=null&&findMenuAll.size()>0){
            responseResult.setResult(findMenuAll);
        }else {
            //缓存为空,则重新查询,并存入缓存
            List<MenuInfo> all = menuService.findAll(0L);
            redisTemplate.opsForList().leftPushAll("findMenuAll",all);
            redisTemplate.expire("findMenuAll",60, TimeUnit.SECONDS);
            responseResult.setResult(all);
        }
        responseResult.setCode(200);
        responseResult.setSuccess("查询成功");
        return responseResult;
    }

    @RequestMapping("findMenuBySan")
    @ApiOperation(value = "获取没有最后一级的菜单,并将获取到的菜单,存入redis缓存")
    public ResponseResult findMenuBySan(){
        ResponseResult responseResult = new ResponseResult();
        //获取redis缓存中的参数
       /* List<MenuInfo> findMenuAll = redisTemplate.opsForList().range("findMenuBySan", 0, -1);
        //redis缓存不为空,则返回缓存中的参数
        if(findMenuAll!=null&&findMenuAll.size()>0){
            responseResult.setResult(findMenuAll);
        }else {*/
            //缓存为空,则重新查询,并存入缓存
            List<MenuInfo> all = menuService.findMenuBySan(0L);
          /*  redisTemplate.opsForList().leftPushAll("findMenuBySan",all);
            redisTemplate.expire("findMenuBySan",60, TimeUnit.SECONDS);*/
            responseResult.setResult(all);
        /*}*/
        responseResult.setCode(200);
        responseResult.setSuccess("查询成功");
        return responseResult;
    }
    @RequestMapping("findMenuByParentId")
    @ApiOperation(value = "获取菜单的4级子菜单")
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
    public ResponseResult insertMenu(@RequestBody MenuInfo menuInfo){
        ResponseResult responseResult = new ResponseResult();
        Integer integer = menuService.insertMenu(menuInfo);
        responseResult.setCode(200);
        responseResult.setSuccess("添加成功");
        return responseResult;
    }
    @RequestMapping("updateMenu")
    @ApiOperation(value = "修改菜单")
    public ResponseResult updateMenu(@RequestBody MenuInfo menuInfo){
        ResponseResult responseResult = new ResponseResult();
        Integer integer = menuService.updateMenu(menuInfo);
        responseResult.setCode(200);
        responseResult.setSuccess("修改成功");
        return responseResult;
    }
    @RequestMapping("deleteMenu")
    @ApiOperation(value = "删除菜单")
    public ResponseResult deleteMenu(@RequestBody Map map){
        Long id = Long.valueOf(map.get("id").toString());
        ResponseResult responseResult = new ResponseResult();
        Integer integer = menuService.deleteMenu(id);
        responseResult.setCode(200);
        responseResult.setSuccess("删除成功");
        return responseResult;
    }
}
