package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class SpecGroupDTO extends BrandDTO{

    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不为空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "类型id",example = "1")
    @NotNull(message = "类型id不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Integer cid;

    @ApiModelProperty(value = "规格组名称",example = "1")
    @NotEmpty(message = "规格组名不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String name;
}
