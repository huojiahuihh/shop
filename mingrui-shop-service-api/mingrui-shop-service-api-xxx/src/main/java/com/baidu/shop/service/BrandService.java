package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.validate.group.MingruiOperation;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = "品牌类型接口")
@Validated
public interface BrandService {

    @ApiOperation(value = "品牌信息查询")
    @GetMapping(value = "brand/list")
    Result<PageInfo<BrandEntity>> getBrandInfo(@SpringQueryMap BrandDTO brandDTO);

    @ApiOperation(value = "品牌新增")
    @PostMapping(value = "brand/save")
    Result<JsonObject> saveBrand(@Validated({MingruiOperation.Add.class})@RequestBody BrandDTO brandDTO);

    @ApiOperation(value = "品牌修改")
    @PutMapping(value = "brand/save")
    Result<JsonObject> editBrand(@Validated({MingruiOperation.Update.class}) @RequestBody BrandDTO brandDTO);

    @ApiOperation(value = "品牌删除")
    @DeleteMapping(value = "brand/del")
    Result<JsonObject> delBrand(@NotNull Integer id);

    @ApiOperation(value = "通过分类id获取品牌")
    @GetMapping(value = "brand/getBrandInfoByCategoryId")
    Result<List<BrandEntity>> getBrandInfoByCategoryId(@NotNull Integer cid);

    @ApiOperation(value = "通过品牌Id集合获取品牌")
    @GetMapping(value = "brand/getBrandByIdList")
    Result<List<BrandEntity>> getBrandByIdList(@RequestParam String ids);
}
