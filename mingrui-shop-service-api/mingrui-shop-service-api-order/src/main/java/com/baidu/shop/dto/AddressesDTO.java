package com.baidu.shop.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
@ApiModel(value = "获取收货地址")
public class AddressesDTO {

    @ApiModelProperty(value = "收货地址id", example = "1")
    private Integer id;

    private String name;

    private String phone;

    private String state;

    private String city;

    private String district;

    private String address;

    private Integer zipCode;

    private Integer userId;

    private Integer defaultAdd;
}
