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
@ApiModel(value = "菜单表")
public class MenuInfo extends BaseAuditable  implements Serializable {
    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    @ApiModelProperty(value = "上级菜单")
    private Long parentId;
    @ApiModelProperty(value = "菜单登记")
    private Integer leval;
    @ApiModelProperty(value = "菜单访问权限")
    private String url;
    @ApiModelProperty(value = "拥有当前菜单的角色")
    private List<MenuInfo> menuInfoList;
}
