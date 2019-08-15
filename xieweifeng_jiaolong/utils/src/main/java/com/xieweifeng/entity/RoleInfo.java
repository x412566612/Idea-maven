package com.xieweifeng.entity;

import com.xieweifeng.base.BaseAuditable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: LCG
 * 日期: 2019/8/4 16:30
 * 描述:
 */

@Data
@ApiModel(value = "角色表")
public class RoleInfo extends BaseAuditable  implements Serializable {
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色描述")
    private String miaoShu;
    @ApiModelProperty(value = "和当前角色关联的所有用户")
    private String users;
    @ApiModelProperty(value = "角色拥有的菜单")
    private List<MenuInfo> menuInfoList;
    @ApiModelProperty(value = "角色拥有的菜单ID")
    private Long[] menuIds;
    @ApiModelProperty(value = "父角色ID")
    private Long parentId;
    @ApiModelProperty(value = "角色等级")
    private Integer leval;
}
