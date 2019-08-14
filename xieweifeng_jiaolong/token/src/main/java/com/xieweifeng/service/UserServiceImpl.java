package com.xieweifeng.service;/*
@author 谢唯峰
@create 2019-08-06-9:34
*/

import com.xieweifeng.dao.UserDao;
import com.xieweifeng.entity.MenuInfo;
import com.xieweifeng.entity.RoleInfo;
import com.xieweifeng.entity.UserInfo;
import com.xieweifeng.exception.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;


    @Override
    public UserInfo login(String loginName) throws LoginException {
        UserInfo user = userDao.findUserByLoginNameEquals(loginName);
        if(user!=null){
            RoleInfo userId = userDao.findByUserId(user.getId());
            if(userId!=null){
                HashMap<String, String> authmap = new HashMap<>();
                List<MenuInfo> menuInfoByRoleId = userDao.findMenuInfoByRoleId(0L, user.getId());
                this.findMenu(0L,authmap,userId.getId());
                user.setAuthmap(authmap);
                user.setListMenuInfo(menuInfoByRoleId);
                return user;
            }
        }else {
            throw new LoginException("用户名或密码错误");
        }
        return null;
    }

    @Override
    public void findMenu(Long parentid, Map<String, String> map, Long roleId) {
        List<MenuInfo> menuInfoByRoleId = userDao.findMenuInfoByRoleId(parentid, roleId);
        for (MenuInfo m : menuInfoByRoleId) {
            this.findMenu(m.getId(), map, roleId);
            if(m.getLeval()!=1&&m.getUrl()!=null){
                map.put(m.getUrl(),"");
            }
        }

    }


}
