package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "商品详情Html静态化接口")
public interface TemplateService {

    @GetMapping(value = "template/createStaticHTMLTemplate")
    @ApiOperation(value = "通过spuId创建html文件")
    Result<JSONObject> createStaticHTMLTemplate(Integer spuId);

    @GetMapping(value = "template/initStaticHTMLTemplate")
    @ApiOperation(value = "初始化html文件")
    Result<JSONObject> initStaticHTMLTemplate();

    @GetMapping(value = "template/clearStaticHTMLTemplate")
    @ApiOperation(value = "清空html文件")
    Result<JSONObject> clearStaticHTMLTemplate();

    @GetMapping(value = "template/deleteStaticHTMLTemplate")
    @ApiOperation(value = "通过spuId删除html文件")
    Result<JSONObject> deleteStaticHTMLTemplate(Integer spuId);

}