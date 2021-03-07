package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.document.GoodsDoc;
import com.baidu.shop.response.GoodsResponse;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "es的接口")
public interface ShopElasticsearchService {

//    @ApiOperation(value = "获取商品测试信息")
//    @GetMapping(value = "es/goodsInfo")
//    Result<JsonObject> esGoodsInfo();


    @ApiOperation(value = "es商品的初始化.")
    @GetMapping(value = "es/initGoodsEsData")
    Result<JsonObject> initGoodsEsData();

    @ApiOperation(value = "清空Es中的商品数据")
    @GetMapping(value = "es/clearGoodsEsData")
    Result<JsonObject> clearGoodsEsData();


    @ApiOperation(value = "搜索")
    @GetMapping(value = "es/search")
    GoodsResponse search(@RequestParam String search, @RequestParam Integer page, @RequestParam String filter);
}
