package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SkuEntity;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = "商品接口")
@Validated
public interface GoodsService {

    @ApiOperation(value = "获取spu的信息")
    @GetMapping(value = "goods/getSpuInfo")
    Result<List<SpuDTO>> getSpuInfo(@SpringQueryMap SpuDTO spuDTO);

    @ApiOperation(value = "新增商品")
    @PostMapping(value = "goods/save")
    Result<JSONObject> saveGoods( @RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "通过获取spu查询SpuDetail的详细信息")
    @GetMapping(value = "goods/getSpuDetailBySpuId")
    Result<SpuDetailEntity> getSpuDetailBySpuId(@RequestParam Integer spuId);

    @ApiOperation(value = "通过spuId获取skuId")
    @GetMapping(value = "goods/getSkuIdBySpuId")
    Result<List<SkuDTO>> getSkuIdBySpuId(@NotNull @RequestParam Integer spuId);

    @ApiOperation(value = "通过spuId上下架skuId")
    @GetMapping(value = "goods/updown")
    Result<JsonObject> goodsUpdown(@NotNull Integer spuId);

    @ApiOperation(value = "修改商品")
    @PutMapping(value = "goods/save")
    Result<JsonObject> editGoods(@RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "删除商品")
    @DeleteMapping(value = "good/delete")
    //@RequestBody 来接收前端传递给后端的json字符串中的数据
    Result<JsonObject> deleteGoods(@NotNull Integer spuId);

    @ApiOperation(value = "通过skuId查询sku信息")
    @GetMapping(value = "/goods/getSkuById")
    Result<SkuEntity> getSkuById(@RequestParam Long skuId);
}
