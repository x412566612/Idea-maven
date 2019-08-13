package com.xieweifeng.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel(value = "公共字段的实体类")
public abstract class BaseAuditable  implements Serializable {


    @Id
    @ApiModelProperty(value = "主键",dataType = "Long")
    @JsonSerialize(using = ToStringSerializer.class)
    Long id;

    @LastModifiedDate
    @ApiModelProperty(value = "最后一次修改时间",dataType = "Date")
    Date updateTime;

    @CreatedDate
    @ApiModelProperty(value = "创建时间",dataType = "Date")
    Date createTime;

    @Version
    @ApiModelProperty(value = "版本号",dataType = "Long")
    private Long version;
}
