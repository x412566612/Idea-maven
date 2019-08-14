package com.xieweifeng.entity;/*
@author 谢唯峰
@create 2019-08-05-20:45
*/

import com.xieweifeng.base.BaseAuditable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "这是一个用户的实体类")
public class UserInfo  extends BaseAuditable implements Serializable {
    @ApiModelProperty(value = "父用户ID",dataType = "long")
    private long parentId;// '用来描述该用户是属于那个用户的子用户',
    @ApiModelProperty(value = "用户名",dataType = "String")
    private String userName;
    @ApiModelProperty(value = "登录名",dataType = "String")
    private String loginName;
    @ApiModelProperty(value = "密码",dataType = "String")
    private String password;
    @ApiModelProperty(value = "性别",dataType = "int")
    private Integer sex;
    @ApiModelProperty(value = "手机号",dataType = "String")
    private String tel;
    @ApiModelProperty(value = "头像",dataType = "String")
    private String portrait;
    @ApiModelProperty(value = "用户的访问权限")
    private Map<String,String> authmap;
    @ApiModelProperty(value = "用户的角色拥有的菜单")
    private List<MenuInfo> listMenuInfo;
    @ApiModelProperty(value = "用户关联的角色")
    private RoleInfo roleInfo;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    private String sexs;
    public void setSex(int sex) {
        this.sex = sex;
        if(this.sexs==null){
           this.sexs = sex==0?"女":"男";
        }

    }

    public void setSexs(String sexs) {
        this.sexs = sexs;
        if(this.sex==null){
            this.sex = sexs.equals("男")?1:0;
        }
    }
}
