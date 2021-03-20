package com.baidu.shop.entity;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_Addresses")
public class AddressesEntity {

    @Id
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
