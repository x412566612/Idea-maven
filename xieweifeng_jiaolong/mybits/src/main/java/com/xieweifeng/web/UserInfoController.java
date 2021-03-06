package com.xieweifeng.web;/*
@author 谢唯峰
@create 2019-08-07-14:26
*/

import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.xieweifeng.dao.UserInfoDao;
import com.xieweifeng.entity.Condition;
import com.xieweifeng.entity.ResponseResult;
import com.xieweifeng.entity.UserInfo;
import com.xieweifeng.service.UserInfoService;
import com.xieweifeng.utils.MD5;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "对用户进行CRUD操作")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @RequestMapping("findByUserInfo")
    @ApiOperation(value = "对用户进行查询,条件用户名的模糊查询,创建时间的区间查询和性别的查询")
    @ApiImplicitParam(
            name = "condition",
            value = "将传入的参数封装成一个自定义条件类"
    )
    public ResponseResult findByNameAndDateAndSex(@RequestBody Condition condition){
        //condition,自定义查询的条件类
        ResponseResult responseResult = new ResponseResult();
        PageInfo<UserInfo> pageInfo = userInfoService.findByNameAndDateAndSex(condition);
        responseResult.setResult(pageInfo);
        responseResult.setCode(200);
        responseResult.setSuccess("查询成功");
        return responseResult;
    }
    @CrossOrigin
    @RequestMapping("savePortrait")
    @ApiOperation(value = "上传图片")
    @ApiImplicitParam(
            name = "file",
            value = "前端提交的图片文件"
    )
    public void savePortrait(@RequestParam("file") MultipartFile file) throws IOException {
        if(file!=null){
            file.transferTo(new File("D:\\Eclipse-img\\"+file.getOriginalFilename()));
            System.out.println("添加图片"+file.getOriginalFilename());
        }else {
            System.out.println("图片为空");
        }
    }
   /* @CrossOrigin
    @RequestMapping("savePortrait")
    @ApiOperation(value = "上传图片")
    public void testUploadAndCreateThumb(@RequestParam("file") MultipartFile file) throws IOException {
        if(file!=null){
           *//* file.transferTo(new File("D:\\Eclipse-img\\"+file.getOriginalFilename()));
            File file1 = new File("D:\\Eclipse-img\\"+file.getOriginalFilename());*//*
            // 上传并且生成缩略图
            StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                    file.getInputStream(), file.getSize(), "png", null);
            // 带分组的路径
            System.out.println(storePath.getFullPath());
            // 不带分组的路径
            System.out.println(storePath.getPath());
            // 获取缩略图路径
            String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
            System.out.println(path);
        }else {
            System.out.println("图片为空");
        }
    }*/



    @CrossOrigin
    @RequestMapping("deletePortrait")
    @ApiOperation(value = "删除,除默认图片之外的图片")
    @ApiImplicitParam(
            name = "file",
            value = "根据文件名称,删除指定图片"
    )
    public void deletePortrait(String file) throws IOException {

        if(!file.equals("timg (10).jpg")&&!file.equals("timg (1).jpg")){
            System.out.println("删除图片"+file);
            File file1 = new File("D:\\Eclipse-img\\" + file);
            if(file1.exists()){
                file1.delete();
            }
        }else {
            System.out.println("不能删除删除默认图片");
        }
    }
    @RequestMapping("insertUserInfo")
    @ApiOperation(value = "添加一个用户")
    @ApiImplicitParam(
            name = "userInfo",
            value = "传入一个用户对象,用于进行添加操作"
    )
    public ResponseResult insertUserInfo(@RequestBody UserInfo userInfo){
        ResponseResult responseResult = new ResponseResult();

        Integer integer = userInfoService.insertUserInfo(userInfo);
        responseResult.setResult(integer);
        responseResult.setCode(200);
        responseResult.setSuccess("添加用户成功");
        return responseResult;
    }
    @RequestMapping("updateUserInfo")
    @ApiOperation(value = "修改一个用户")
    @ApiImplicitParam(
            name = "userInfo",
            value = "传入一个用户对象,用于进行修改操作"
    )
    public ResponseResult updateUserInfo(@RequestBody UserInfo userInfo){

        Integer integer = userInfoService.updateUserInfo(userInfo);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setResult(integer);
        responseResult.setCode(200);
        responseResult.setSuccess("修改成功");
        return responseResult;
    }
    @RequestMapping("deleteUserInfo")
    @ApiOperation(value = "逻辑删除或恢复用户")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为id的Long参数和一个名称为status的Integer参数"
    )
    public ResponseResult deleteUserInfo(@RequestBody Map map){
        Long id =Long.valueOf(map.get("id").toString()) ;
        Integer status = Integer.valueOf(map.get("status").toString());
        Integer integer = userInfoService.deleteUserInfo(id,status);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setResult(integer);
        responseResult.setCode(200);
        String s = status==1?"恢复":"删除";
        responseResult.setSuccess(s+"成功");
        return responseResult;
    }

    @RequestMapping("insertUserAndRoles")
    @ApiOperation(value = "关联用户的角色")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为userId的Long参数和一个名称为roleId的Long参数"
    )
    public ResponseResult insertUserAndRoles(@RequestBody Map<String,Long> map){
        Integer integer = userInfoService.insertUserAndRoles(map.get("userId"),map.get("roleId"));
        ResponseResult responseResult = new ResponseResult();
        responseResult.setResult(integer);
        responseResult.setCode(200);
        responseResult.setSuccess("添加成功");
        return responseResult;
    }
    @RequestMapping("findUserInfoById")
    @ApiOperation(value = "根据ID查询用户")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为id的Long参数"
    )
    public ResponseResult findUserInfoById(@RequestBody Map map){
        Long id = Long.valueOf(map.get("id").toString());
        ResponseResult responseResult = new ResponseResult();
        UserInfo userInfoById = userInfoService.findUserInfoById(id);
        responseResult.setResult(userInfoById);
        responseResult.setCode(200);
        responseResult.setSuccess("查询成功");
        return responseResult;
    }

    @RequestMapping("findUserByRoleId")
    @ApiOperation(value = "根据角色ID查询用户是否存在")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为id的Long参数"
    )
    public ResponseResult findUserByRoleId(@RequestBody Map map){
        String id1 = map.get("id").toString();
        Long id = Long.valueOf(id1);
        ResponseResult responseResult = new ResponseResult();
        List<UserInfo> userByRoleId = userInfoService.findUserByRoleId(id);
        if(userByRoleId.size()==0){
            responseResult.setCode(200);
            responseResult.setSuccess("未绑定其他参数,可以删除");
        }else{
            responseResult.setCode(500);
            responseResult.setSuccess("绑定了其他参数,不能删除");
        }
        return responseResult;
    }

    @RequestMapping("findUserInfoByLoginName")
    @ApiOperation(value = "查询登录名是否存在")
    @ApiImplicitParam(
            name = "map",
            value = "将传入的参数封装成一个map集合,里面必须携带一个名称为loginName的字符串参数"
    )
    public ResponseResult findUserInfoByLoginName(@RequestBody Map map){
        String loginName = map.get("loginName").toString();
        ResponseResult responseResult = new ResponseResult();
        UserInfo userInfoByLoginName = userInfoService.findUserInfoByLoginName(loginName);
        if(userInfoByLoginName==null){
            responseResult.setCode(200);
            responseResult.setSuccess("用户名可以使用");
        }else {
            responseResult.setCode(500);
            responseResult.setSuccess("用户已存在");
        }

        return responseResult;
    }

    @RequestMapping("createExcel")
    @ApiOperation(value = "导出所有的用户")
    public  void createExcel() throws IOException {
        userInfoService.createExcel();
    }
}
