package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpuDetailEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Api(tags = "商品接口")
public interface GoodsService {

    @ApiOperation(value = "获取spu的信息")
    @GetMapping(value = "goods/getSpuInfo")
    Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO);

    @ApiOperation(value = "新增商品")
    @PostMapping(value = "goods/save")
    Result<JSONObject> saveGoods(@RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "获取spu查询SpuDetail的详细信息")
    @GetMapping(value = "goods/getSpuDetailBySpuId")
    Result<SpuDetailEntity> getSpuDetailBySpuId(Integer spuId);

    @ApiOperation(value = "通过spuId获取skuId")
    @GetMapping(value = "goods/getSkuIdBySpuId")
    Result<List<SkuDTO>> getSkuIdBySpuId(Integer spuId);
}
