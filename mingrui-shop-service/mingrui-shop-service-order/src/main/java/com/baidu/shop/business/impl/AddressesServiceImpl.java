package com.baidu.shop.business.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.business.AddressesService;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.dto.AddressesDTO;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.AddressesEntity;
import com.baidu.shop.entity.CityEntity;
import com.baidu.shop.entity.OrderDetailEntity;
import com.baidu.shop.mapper.AddressesMapper;
import com.baidu.shop.mapper.CityMapper;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.JwtUtils;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class AddressesServiceImpl extends BaseApiService implements AddressesService {


    @Autowired
    private JwtConfig jwtConfig;

    @Resource
    private AddressesMapper addressesMapper;

    @Resource
    private CityMapper cityMapper;

    @Override
    public Result<List<AddressesEntity>> getAddress(String token) {

        List<AddressesEntity> addressesEntities = null;
        try {
            //获取当前登录用户
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            Example example = new Example(AddressesEntity.class);
            Example.Criteria criteria = example.createCriteria();
            example.createCriteria().andEqualTo("userId",infoFromToken.getId());
            addressesEntities = addressesMapper.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.setResultSuccess(addressesEntities);
    }

    @Override
    public Result<JsonObject> delAddress(Integer id,String token) {

        try {
            //获取当前登录的用户
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            Example example = new Example(AddressesEntity.class);
            example.createCriteria().andEqualTo("id",id);
            addressesMapper.deleteByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.setResultSuccess();
    }

    @Override
    public Result<List<CityEntity>> getCity(Integer parentId) {

        Example example = new Example(CityEntity.class);
        example.createCriteria().andEqualTo("parentId",parentId);
        List<CityEntity> cityEntities = cityMapper.selectByExample(example);
        return this.setResultSuccess(cityEntities);
    }

    @Override
    public Result<JsonObject> saveAddress(AddressesDTO addressesDTO ,String token) {

        try {
            //获取当前登录用户
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            addressesDTO.setUserId(userInfo.getId());
            AddressesEntity addressesEntity = BaiduBeanUtil.copyProperties(addressesDTO, AddressesEntity.class);
            addressesMapper.insertSelective(addressesEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> editAddress(AddressesDTO addressesDTO, String token) {

        try {
            //获取当前登录用户
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            addressesDTO.setUserId(addressesDTO.getId());
            AddressesEntity addressesEntity = BaiduBeanUtil.copyProperties(addressesDTO, AddressesEntity.class);
            addressesMapper.updateByPrimaryKey(addressesEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.setResultSuccess();
    }


}
