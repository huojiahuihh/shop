package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandCategoryEntity;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.baidu.shop.utils.PinyinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Resource
    private BrandMapper mapper;

    //查询
    @Override
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO) {

        //mybatis 分页插件 mybatis执行器
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());
        //排序
        //第四种办法
        if (!StringUtils.isEmpty(brandDTO.getSort())) PageHelper.orderBy(brandDTO.getOrderBy());

//        String order="";
//        if(!StringUtils.isEmpty(brandDTO.getOrder())){
        //第一种 用if和else判断是降序还是升序
//            if (Boolean.valueOf(brandDTO.getOrder())){
//                order = "desc";
//            }else {
//                order = "asc";
//            }
        //第二种 通过三目运算 判断是用降序还是升序
//            PageHelper.orderBy(brandDTO.getSort() + " " + (Boolean.valueOf(brandDTO.getOrder())?"desc":"asc"));
//        }
        //第三种
//        PageHelper.orderBy(brandDTO.getOrderBy());

        //查询与分页
        BrandEntity brandEntity = new BrandEntity();
        BeanUtils.copyProperties(brandDTO,brandEntity);

        Example example = new Example(BrandEntity.class);
        example.createCriteria().andLike("name","%" + brandEntity.getName() +"%");

        List<BrandEntity> brandEntities = mapper.selectByExample(example);
        PageInfo<BrandEntity> pageInfo = new PageInfo<>(brandEntities);

        return this.setResultSuccess(pageInfo);
    }

    @Resource   //中间表
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    @Transactional
    public Result<JsonObject> saveBrand(BrandDTO brandDTO) {

        //判断是否为空
        if(!ObjectUtil.isEmpty(brandDTO.getCategories())) return this.setResultError("分类id不能为空");
        //新增返回主键
        //两种实现方式 select-key insert 加两个属性
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        //处理品牌首字母
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        mapper.insertSelective(brandEntity);

        //维护中间表数据
        //判断分类集合字符串是否包含,
        //判断 如果有 , 就说明新增多条数据 分割开来进行新增
        this.insertCreateBrand(brandDTO.getCategories(),brandEntity.getId());
        return this.setResultSuccess("新增成功");
    }


    //修改
    @Override
    @Transactional
    public Result<JsonObject> editBrand(BrandDTO brandDTO) {
        //把brandDTO复制到BrandEntity中  克隆
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        //brandEntity.getName().toCharArray()[0] 转换成一个char类型的数组 获取下标的第一位
        //String.valueOf(brandEntity.getName().toCharArray()[0]  再转换成一个String类型的  因为PinyinUtil.getUpperCase要String类型
        // false 表示首字母
        //又因为 set 只要char类型的 不要String类型的
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        mapper.updateByPrimaryKeySelective(brandEntity);

        //通过brandId删除中间表的数据
        this.deleteCreateBrandById(brandEntity.getId());
        return this.setResultSuccess();
    }

    //删除
    @Override
    public Result<JsonObject> delBrand(Integer id) {
        //删除品牌
        mapper.deleteByPrimaryKey(id);
        //通过brandId删除中间表的数据
        this.deleteCreateBrandById(id);
        return this.setResultSuccess("");
    }

    @Override
    public Result<List<BrandEntity>> getBrandInfoByCategoryId(Integer cid) {
        List<BrandEntity> list = mapper.getBrandInfoByCategoryId(cid);
        return this.setResultSuccess(list);
    }


    //提出来公共的部分 使代码更简洁
    private void insertCreateBrand(String categories,Integer brandId){
        //批量新增 \ 新增
        if (StringUtils.isEmpty(categories)) throw new RuntimeException();
        //判断 如果有 , 就说明新增多条数据 分割开来进行新增
        if (categories.contains(",")){
            categoryBrandMapper.insertList(
                Arrays.asList(categories.split(","))
                    .parallelStream()
                    .map(str -> new BrandCategoryEntity(Integer.valueOf(str),brandId))
                    .collect(Collectors.toList())
            );
        //否则就是新增一条数据 普通的新增
        }else {
            BrandCategoryEntity entity = new BrandCategoryEntity(Integer.valueOf(categories),brandId);
            categoryBrandMapper.insertSelective(entity);
        }
    }

    //提出来公共部分 删除中间表的数据
    private void deleteCreateBrandById(Integer brandId){
        //通过brandId删除中间表的数据
        Example example = new Example(BrandCategoryEntity.class);
        example.createCriteria().andEqualTo("brandId",brandId);
        categoryBrandMapper.deleteByExample(example);
    }


}
