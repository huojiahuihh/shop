package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.dto.SpuDetailDTO;
import com.baidu.shop.entity.*;
import com.baidu.shop.mapper.*;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GoodsServiceImpl extends BaseApiService implements GoodsService {

    @Resource
    private SpuMapper spuMapper;

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SpuDetailMapper spuDetailMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private StockMapper stockMapper;

    @Override
    public Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO) {

        Example example = new Example(SpuEntity.class);
        Example.Criteria criteria = example.createCriteria();

        //判断一下是否上架  spuDTO数据传输
        if (ObjectUtil.isNotNull(spuDTO.getSaleable()) && spuDTO.getSaleable()<2){
            criteria.andEqualTo("saleable",spuDTO.getSaleable());
        }

        //用那个框框用来搜索查询 模糊匹配andLike
        if (!StringUtils.isEmpty(spuDTO.getTitle())){
            criteria.andLike("title","%" + spuDTO.getTitle() + "%");
        }

        //分页 用了 baseDTO类的方法   baseBTO是分页的工具类
        if (spuDTO.getPage() != null && spuDTO.getRows()!= null)
            PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());

        //排序
        if (!StringUtils.isEmpty(spuDTO.getSort()) && !StringUtils.isEmpty(spuDTO.getOrder()))
            PageHelper.orderBy(spuDTO.getOrderBy());

        //用来查询sql
        List<SpuEntity> spuEntities = spuMapper.selectByExample(example);

        //商品分类 和 品牌传到前台是数字 需要用来转换一下 因为用户看不懂
        List<SpuDTO> spuDTOList = spuEntities.stream().map(spuEntity -> {
            SpuDTO spuDTO1 = BaiduBeanUtil.copyProperties(spuEntity, SpuDTO.class);
            //通过分类Id集合查询数据
            List<CategoryEntity> categoryEntities = categoryMapper.selectByIdList(Arrays.asList(spuEntity.getCid1(), spuEntity.getCid2(), spuEntity.getCid3()));

            //遍历结合 并且将分类名称用 "~" 拼接
            String categoryName = categoryEntities.stream().map(categoryEntity -> categoryEntity.getName()).collect(Collectors.joining("~"));
            spuDTO1.setCateGoryName(categoryName);

            BrandEntity brandEntity = brandMapper.selectByPrimaryKey(spuEntity.getBrandId());
            spuDTO1.setBrandName(brandEntity.getName());
            return spuDTO1;
        }).collect(Collectors.toList());

        //分页  这个才是分页最后的关键
        PageInfo<SpuEntity> spuEntityPageInfo = new PageInfo<>(spuEntities);
        //返回分页以后的数据
//        return this.setResultSuccess(spuEntityPageInfo);
        return this.setResult(HTTPStatus.OK,spuEntityPageInfo.getTotal() + "" ,spuDTOList);
    }

    @Override
    @Transactional
    public Result<JSONObject> saveGoods(SpuDTO spuDTO) {

        //要是直接 new date 的话 CreateTime  和  LastUpdateTime 两次的时间有差别 不一致
        //加这个final 是为了防止 时间被修改
        final Date date = new Date();
        //新增spu  新增spu的时候返回主键
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setSaleable(1);
        spuEntity.setValid(1);
        spuEntity.setCreateTime(date);
        spuEntity.setLastUpdateTime(date);
        spuMapper.insertSelective(spuEntity);

        //新增spuDetail
        SpuDetailDTO spuDetail = spuDTO.getSpuDetail();
        SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDetail, SpuDetailEntity.class);
        spuDetailEntity.setSpuId(spuEntity.getId());
        spuDetailMapper.insertSelective(spuDetailEntity);

        //新增sku
        //因为和修改用到的代码重复了  所以提出来了  saveSkuAndStockInfo
        this.saveSkuAndStockInfo(spuDTO,spuEntity.getId(),date);
        return this.setResultSuccess();
    }

    //获取spu查询SpuDetail的详细信息
    @Override
    public Result<SpuDetailEntity> getSpuDetailBySpuId(Integer spuId) {
        SpuDetailEntity spuDetailEntity = spuDetailMapper.selectByPrimaryKey(spuId);
        return this.setResultSuccess(spuDetailEntity);
    }

    //通过SpuId获取SkuId
    @Override
    public Result<List<SkuDTO>> getSkuIdBySpuId(Integer spuId) {
        List<SkuDTO> list = skuMapper.getSkuAndStockBySpuId(spuId);
        return this.setResultSuccess(list);
    }

    @Override
    @Transactional
    public Result<JsonObject> editGoods(SpuDTO spuDTO) {
        final Date date = new Date();
        //修改 spu
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setLastUpdateTime(date);
        spuMapper.updateByPrimaryKeySelective(spuEntity);

        //修改spuDetail
        spuDetailMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(),SpuDetailEntity.class));

//        //修改sku   //通过spuId查询sku的信息   // 删除完之后再新增
//        Example example = new Example(SkuEntity.class);
//        example.createCriteria().andEqualTo("spuId",spuDTO);
//        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
//
//        //得到sku集合
//        List<Long> skuIdList = skuEntities.stream().map(skuEntity -> skuEntity.getId()).collect(Collectors.toList());
//        skuMapper.deleteByIdList(skuIdList); //通过skuId结合删除sku信息
//        stockMapper.deleteByIdList(skuIdList); //通过skuId集合删除stock信息
        this.deleteSkuAndStock(spuDTO.getId());
        //新增sku
        this.saveSkuAndStockInfo(spuDTO,spuEntity.getId(), date);
        //修改stock
        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> deleteGoods(Integer spuId) {
        //删除spu
        spuMapper.deleteByPrimaryKey(spuId);
        //删除SpuDetail
        spuMapper.deleteByPrimaryKey(spuId);

//        //删除spu 和 stock
//        //通过 spuId 查询 sku信息
//        Example example = new Example(SkuEntity.class);
//        example.createCriteria().andEqualTo("spuId",spuId);
//        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
//        //得到sku集合
//        List<Long> skuIdList = skuEntities.stream().map(skuEntity -> skuEntity.getId()).collect(Collectors.toList());
//        skuMapper.deleteByIdList(skuIdList);//通过skuId集合删除sku信息
//        stockMapper.deleteByIdList(skuIdList);//通过skuId集合删除stock信息

        this.deleteSkuAndStock(spuId);
        return this.setResultSuccess();
    }


    //新增和修改的代码重复了 所以就提出来当做公共的代码 是页面更整洁
    private void saveSkuAndStockInfo(SpuDTO spuDTO,Integer spuId,Date date){
        //新增sku
        List<SkuDTO> skus = spuDTO.getSkus();
        skus.stream().forEach(skuDTO -> {

            SkuEntity skuEntity = BaiduBeanUtil.copyProperties(skuDTO, SkuEntity.class);
            skuEntity.setSpuId(spuId);
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            skuMapper.insertSelective(skuEntity);

            //新增stock
            StockEntity stockEntity = new StockEntity();
            stockEntity.setSkuId(skuEntity.getId());
            stockEntity.setStock(skuDTO.getStock());
            stockMapper.insertSelective(stockEntity);
        });
    }
    private void deleteSkuAndStock(Integer spuId){
        //删除spu 和 stock
        //通过 spuId 查询 sku信息
        Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuId);
        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
        //得到sku集合
        List<Long> skuIdList = skuEntities.stream().map(skuEntity -> skuEntity.getId()).collect(Collectors.toList());
        skuMapper.deleteByIdList(skuIdList);//通过skuId集合删除sku信息
        stockMapper.deleteByIdList(skuIdList);//通过skuId集合删除stock信息
    }

}
