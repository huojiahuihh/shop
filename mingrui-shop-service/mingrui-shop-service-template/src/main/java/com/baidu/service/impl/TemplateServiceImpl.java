package com.baidu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.feign.BrandFeign;
import com.baidu.feign.CategoryFeign;
import com.baidu.feign.GoodsFeign;
import com.baidu.feign.SpecificationFeign;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.*;
import com.baidu.shop.entity.*;
import com.baidu.shop.service.TemplateService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TemplateServiceImpl extends BaseApiService implements TemplateService {

    //注入静态模板
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private BrandFeign brandFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private SpecificationFeign specificationFeign;

    @Value(value = "${mrshop.static.html.path}")
    private String htmlPath;

    //通过spuId创建html文件
    @Override
    public Result<JSONObject> createStaticHTMLTemplate(Integer spuId) {

        //得到要渲染的数据
        Map<String, Object> goodsInfo = this.getGoodsInfo(spuId);
        //创建上下文
        Context context = new Context();
        context.setVariables(goodsInfo);

        //创建要生成的文件
        File file = new File(htmlPath, spuId + ".html");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //要生成的编码字符
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file, "UTF-8");
            templateEngine.process("item", context, printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (ObjectUtil.isNotNull(printWriter)) {
                printWriter.close();//这需要关流 要不然会有问题在删除那
            }
        }
        return this.setResultSuccess();
    }
    private Map<String, Object> getGoodsInfo(Integer spuId) {

        HashMap<String, Object> goodsInfoMap = new HashMap<>();

        //spu信息
        SpuDTO spuResultData = this.getSpuInfo(spuId);
        goodsInfoMap.put("spuInfo",spuResultData);

        //spuDetail信息
        goodsInfoMap.put("spuDetail",this.getSpuDetail(spuId));

        //查询分类信息
        goodsInfoMap.put("categoryInfo",this.getCategoryInfo(spuResultData.getCid1() +"",+spuResultData.getCid2()+"",spuResultData.getCid3()+""));

        //查询品牌信息
        goodsInfoMap.put("brandInfo",this.getBrandInfo(spuResultData.getBrandId()));
        //获取sku的信息
        goodsInfoMap.put("skuInfo",this.getSkuInfo(spuId));

        //规格组和规格参数
        goodsInfoMap.put("specGroupAndParam",this.getSpecGroupAndParam(spuResultData.getCid3()));
        //特有规格参数
        goodsInfoMap.put("specParamMap",this.getSpecParamMap(spuResultData.getCid3()));

        return goodsInfoMap;
    }

    //spu信息
    private SpuDTO getSpuInfo(Integer spuId){
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setId(spuId);
        Result<List<SpuDTO>> spuResult = goodsFeign.getSpuInfo(spuDTO);
        SpuDTO spuResultData = null;
        if (spuResult.isSuccess()){
            spuResultData = spuResult.getData().get(0);
        }
        return spuResultData;
    }


    //spuDetail信息
    private SpuDetailEntity getSpuDetail(Integer spuId){
        SpuDetailEntity spuDetailEntity = null;
        Result<SpuDetailEntity> spuDetailResult = goodsFeign.getSpuDetailBySpuId(spuId);
        if (spuDetailResult.isSuccess()){
            spuDetailEntity = spuDetailResult.getData();
        }
        return spuDetailEntity;
    }

    //查询分类信息
    private List<CategoryEntity> getCategoryInfo(String cid1,String cid2,String cid3){
        List<CategoryEntity> categoryEntityList = null;

        Result<List<CategoryEntity>> categoryResult = categoryFeign.getCategoryByIdList(String.join
                (",", Arrays.asList(cid1,cid2,cid3))
        );
        if (categoryResult.isSuccess()){
            categoryEntityList = categoryResult.getData();
        }
        return categoryEntityList;
    }


    //查询品牌信息
    private BrandEntity getBrandInfo(Integer brandId){

        BrandEntity brandEntity = null;
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brandId);
        Result<PageInfo<BrandEntity>> brandResult = brandFeign.getBrandInfo(brandDTO);
        if (brandResult.isSuccess()){
            brandEntity = brandResult.getData().getList().get(0);
        }
        return brandEntity;
    }

    //获取sku的信息
    private List<SkuDTO> getSkuInfo(Integer spuId){
        List<SkuDTO> skuList = null;
        Result<List<SkuDTO>> skuResult = goodsFeign.getSkuIdBySpuId(spuId);
        if (skuResult.isSuccess()){
            skuList = skuResult.getData();
        }
        return skuList;
    }

    //规格组和规格参数
    private List<SpecGroupDTO> getSpecGroupAndParam(Integer cid3){

        List<SpecGroupDTO> specGroupAndparam = null;

        SpecGroupDTO specGroupDTO = new SpecGroupDTO();
        specGroupDTO.setCid(cid3);
        Result<List<SpecGroupEntity>> specGroupResult = specificationFeign.getSpecGroupInfo(specGroupDTO);
        if (specGroupResult.isSuccess()){
            List<SpecGroupEntity> specGroupList = specGroupResult.getData();
            specGroupAndparam = specGroupList.stream().map(specGroup -> {
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
        }
        return specGroupAndparam;
    }

    //特有规格参数
    private Map<Integer,String> getSpecParamMap(Integer cid3){

        Map<Integer,String>  specParamMap = new HashMap<>();

        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(cid3);
        specParamDTO.setGeneric(false);
        Result<List<SpecParamEntity>> specParamResult = specificationFeign.getSpecParamInfo(specParamDTO);
        if (specParamResult.isSuccess()){
            List<SpecParamEntity> specParamEntityList = specParamResult.getData();
            specParamEntityList.stream().forEach(specParam -> specParamMap.put(specParam.getId(),specParam.getName()));
        }
        return specParamMap;
    }

    @Override //初始化html文件
    public Result<JSONObject> initStaticHTMLTemplate() {
        Result<List<SpuDTO>> spuInfo = goodsFeign.getSpuInfo(new SpuDTO());
        if (spuInfo.isSuccess()){
            List<SpuDTO> spuList = spuInfo.getData();
            spuList.stream().forEach(spu->{
                this.createStaticHTMLTemplate(spu.getId());
            });
        }
        return this.setResultSuccess();
    }

    @Override//清空html文件
    public Result<JSONObject> clearStaticHTMLTemplate() {
        Result<List<SpuDTO>> spuInfo = goodsFeign.getSpuInfo(new SpuDTO());
        if (spuInfo.isSuccess()){
            spuInfo.getData().stream().forEach(spu ->{
                this.deleteStaticHTMLTemplate(spu.getId());
            });
        }
        return this.setResultSuccess();
    }

    @Override //通过spuId删除html文件
    public Result<JSONObject> deleteStaticHTMLTemplate(Integer spuId) {

        File file = new File(htmlPath, spuId + ".html");
        if (file.exists()){
            file.delete();
        }
        return this.setResultSuccess();
    }
}
