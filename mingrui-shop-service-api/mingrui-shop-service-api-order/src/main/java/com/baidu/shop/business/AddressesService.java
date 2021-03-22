package com.baidu.shop.business;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.AddressesDTO;
import com.baidu.shop.entity.AddressesEntity;
import com.baidu.shop.entity.CityEntity;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "地址接口")
public interface AddressesService {

    @ApiOperation(value = "查询地址")
    @GetMapping(value = "address/getAddress")
    Result<List<AddressesEntity>> getAddress(@CookieValue(value="MRSHOP_TOKEN") String token);

    @ApiOperation(value = "删除地址")
    @DeleteMapping(value = "address/delAddress")
    Result<JsonObject> delAddress(Integer id,@CookieValue(value="MRSHOP_TOKEN") String token);

    //查询城市 三级联动
    @ApiOperation(value = "城市查询")
    @GetMapping(value = "city/getCity")
    Result<List<CityEntity>> getCity(Integer parentId);

    //新增收货地址
    @ApiOperation(value = "新增收货地址")
    @PostMapping(value = "address/saveAddress")
    Result<JsonObject> saveAddress(@RequestBody AddressesDTO addressesDTO , @CookieValue(value = "MRSHOP_TOKEN") String token);

    //新增收货地址
    @ApiOperation(value = "修改收货地址")
    @PutMapping(value = "address/editAddress")
    Result<JsonObject> editAddress(@RequestBody AddressesDTO addressesDTO, @CookieValue(value = "MRSHOP_TOKEN") String token);

    //设置默认地址
    @ApiOperation(value = "设为默认地址")
    @PutMapping(value = "address/editDefaultAdd")
    Result<JsonObject> editDefaultAdd(@RequestBody AddressesEntity addressesEntity);



}
