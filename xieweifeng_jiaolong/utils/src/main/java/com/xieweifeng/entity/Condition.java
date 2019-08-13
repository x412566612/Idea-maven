package com.xieweifeng.entity;/*
@author 谢唯峰
@create 2019-08-07-11:06
*/

import io.swagger.annotations.Api;
import lombok.Data;

import java.util.Date;

@Data
@Api(value = "自定义查询条件类")
public class Condition {
    private String userName;
    private Date str;
    private Date end;
    private String sexs;
    private Integer pageNum;
    private Integer pageSize;

}
