package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "t_city")
public class CityEntity {

    private Integer id;

    private String name;

    private Integer parentId;

    private Integer level;
}
