package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpuEntity;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;


@Api(tags = "商品接口")
public interface GoodsService {

    @ApiOperation(value = "获取spu的信息")
    @GetMapping(value = "goods/getSpuInfo")
    public Result<PageInfo<SpuEntity>> getSpuInfo(SpuDTO spuDTO);
}
