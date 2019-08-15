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
import com.xieweifeng.utils.MD5;
import com.xieweifeng.utils.TwitterIdWorker;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
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
                        condition.getSexs(),
                        condition.getPower()
                );
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
        TwitterIdWorker twitterIdWorker = new TwitterIdWorker();
        //使用MD5对用户密码进行加密,salt是加密条件,为预防解密
        String lcg = MD5.encryptPassword(userInfo.getPassword(), "lcg");
        //将加密后的密码存入用户对象
        userInfo.setPassword(lcg);
        userInfo.setId(twitterIdWorker.nextId());
        //设置创建时间
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        Integer integer = userInfoDao.insertUserInfo(userInfo);
        return integer;
    }

    @Override
    @Transactional
    @ApiOperation("修改用户")
    public Integer updateUserInfo(UserInfo userInfo) {
        //使用MD5对用户密码进行加密,salt是加密条件,为预防解密
        String lcg = MD5.encryptPassword(userInfo.getPassword(), "lcg");
        userInfo.setPassword(lcg);
        //设置修改时间
        userInfo.setUpdateTime(new Date());
        userInfo.setVersion(userInfo.getVersion()+1);
        Integer integer = userInfoDao.updateUserInfo(userInfo);
        return integer;
    }

    @Override
    @Transactional
    @ApiOperation("批量删除用户")
    public Integer deleteUserInfo(Long id,Integer status) {
        Integer integer = userInfoDao.deleteUserInfo(id,status);
        return integer;
    }

    @Override
    @ApiOperation("添加用户关联的角色")
    public Integer insertUserAndRoles(Long userId, Long roleId) {
        userInfoDao.deleteUserAndRoles(userId);
        Integer integer = userInfoDao.insertUserAndRoles(userId, roleId);
        return integer;
    }

    @ApiOperation(value = "导出所有的用户")
    public  void createExcel() throws IOException {
        System.out.println("开始导出用户");
        // 获取桌面路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String desktop = fsv.getHomeDirectory().getPath();
        String filePath = desktop + "/template.xls";

        File file = new File(filePath);
        OutputStream outputStream = new FileOutputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("父id");
        row.createCell(2).setCellValue("版本号");
        row.createCell(3).setCellValue("用户名称");
        row.createCell(4).setCellValue("登录名");
        row.createCell(5).setCellValue("密码");
        row.createCell(6).setCellValue("性别");
        row.createCell(7).setCellValue("电话号码");
        row.createCell(8).setCellValue("创建时间");
        row.createCell(9).setCellValue("修改时间");
        row.createCell(10).setCellValue("图片路径");
        row.setHeightInPoints(30); // 设置行的高度

        List<UserInfo> userInfos = userInfoDao.findByUserInfoAll();

        for (Integer i=0;i<userInfos.size();i++){
            UserInfo userInfo = userInfos.get(i);
            HSSFRow row1 = sheet.createRow(i+1);
            row1.createCell(0).setCellValue(userInfo.getId().toString());
            row1.createCell(1).setCellValue(userInfo.getParentId());
            row1.createCell(2).setCellValue(userInfo.getVersion());
            row1.createCell(3).setCellValue(userInfo.getUserName());
            row1.createCell(4).setCellValue(userInfo.getLoginName());
            row1.createCell(5).setCellValue(userInfo.getPassword());
            row1.createCell(6).setCellValue(userInfo.getSex());
            row1.createCell(7).setCellValue(userInfo.getTel());
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            HSSFCreationHelper creationHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
            HSSFCell cell1 = row1.createCell(8);
            cell1.setCellStyle(cellStyle);
            cell1.setCellValue(userInfo.getCreateTime());
            HSSFCell cell2 = row1.createCell(9);
            cell2.setCellStyle(cellStyle);
            cell2.setCellValue(userInfo.getUpdateTime());
            row1.createCell(10).setCellValue(userInfo.getPortrait());
        }
        workbook.setActiveSheet(0);
        workbook.write(outputStream);
        outputStream.close();
        System.out.println("导出用户结束");
    }
}
