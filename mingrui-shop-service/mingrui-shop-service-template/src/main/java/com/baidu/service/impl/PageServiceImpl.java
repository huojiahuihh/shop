package com.baidu.service.impl;

import com.baidu.feign.BrandFeign;
import com.baidu.feign.CategoryFeign;
import com.baidu.feign.GoodsFeign;
import com.baidu.feign.SpecificationFeign;
import com.baidu.service.PageService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.*;
import com.baidu.shop.entity.*;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private BrandFeign brandFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private SpecificationFeign specificationFeign;

    @Override
    public Map<String, Object> getGoodsInfo(Integer spuId) {

        HashMap<String, Object> goodsInfoMap = new HashMap<>();

        //spu信息
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setId(spuId);
        Result<List<SpuDTO>> spuResult = goodsFeign.getSpuInfo(spuDTO);
        SpuDTO spuResultData = null;
        if (spuResult.isSuccess()){
            spuResultData = spuResult.getData().get(0);
            goodsInfoMap.put("spuInfo",spuResultData);
        }
        //spuDetail信息
        Result<SpuDetailEntity> spuDetailResult = goodsFeign.getSpuDetailBySpuId(spuId);
        if (spuDetailResult.isSuccess()){
            goodsInfoMap.put("spuDetail",spuDetailResult.getData());
        }

        //查询分类信息
        Result<List<CategoryEntity>> categoryResult = categoryFeign.getCategoryByIdList(String.join(",",
                Arrays.asList(spuResultData.getCid1() + "",
                              spuResultData.getCid2() + "",
                              spuResultData.getCid3() + ""))
        );
        if (categoryResult.isSuccess()){
            goodsInfoMap.put("categoryInfo",categoryResult.getData());
        }
        //查询品牌信息
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(spuResultData.getBrandId());
        Result<PageInfo<BrandEntity>> brandResult = brandFeign.getBrandInfo(brandDTO);
        if (brandResult.isSuccess()){
            goodsInfoMap.put("brandInfo",brandResult.getData().getList().get(0));
        }
        //获取sku的信息
        Result<List<SkuDTO>> skuResult = goodsFeign.getSkuIdBySpuId(spuId);
        if (skuResult.isSuccess()){
            goodsInfoMap.put("skuInfo",skuResult.getData());
        }

        //规格组和规格参数
        SpecGroupDTO specGroupDTO = new SpecGroupDTO();
        specGroupDTO.setCid(spuResultData.getCid3());
        Result<List<SpecGroupEntity>> specGroupResult = specificationFeign.getSpecGroupInfo(specGroupDTO);
        if (specGroupResult.isSuccess()){
            List<SpecGroupEntity> specGroupList = specGroupResult.getData();
            List<SpecGroupDTO> specGroupAndparam = specGroupList.stream().map(specGroup -> {
                SpecGroupDTO specGroupDTO1 = BaiduBeanUtil.copyProperties(specGroup, SpecGroupDTO.class);
                SpecParamDTO specParamDTO = new SpecParamDTO();
                //通过规格组查询规格参数
                specParamDTO.setGroupId(specGroupDTO1.getId());
                specParamDTO.setGeneric(true);
                Result<List<SpecParamEntity>> specParamResult = specificationFeign.getSpecParamInfo(specParamDTO);
                if (specParamResult.isSuccess()) {
                    specGroupDTO1.setSpecList(specParamResult.getData());
                }
                return specGroupDTO1;
            }).collect(Collectors.toList());
            goodsInfoMap.put("specGroupAndParam",specGroupAndparam);
        }
        //特有规格参数

        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(spuResultData.getCid3());
        specParamDTO.setGeneric(false);
        Result<List<SpecParamEntity>> specParamResult = specificationFeign.getSpecParamInfo(specParamDTO);
        if (specGroupResult.isSuccess()){
            List<SpecParamEntity> specParamEntityList = specParamResult.getData();
            Map<Integer,String>  specParamMap = new HashMap<>();
            specParamEntityList.stream().forEach(specParam -> specParamMap.put(specParam.getId(),specParam.getName()));
            goodsInfoMap.put("specParamMap",specParamMap);
        }
        return goodsInfoMap;
    }


}
