package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.service.CategoryService;
import com.baidu.shop.entity.BrandCategoryEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.utils.ObjectUtil;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {

    @Resource
    private CategoryMapper mapper;

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setParentId(pid);
        List<CategoryEntity> list = mapper.select(categoryEntity);
        return this.setResultSuccess(list);
    }

    @Override
    @Transactional //这个注解是为了回滚  增删改都需要这个注解
    public Result<JsonObject> delCategory(Integer id) {

        //检验一下Id是否合法  id不能为空并且不能是负数
        if(ObjectUtil.isNull(id) || id <= 0) return this.setResultError("id不合法");

        //通过Id查询数据
        CategoryEntity categoryEntity = mapper.selectByPrimaryKey(id);

        //判断当前节点是否存在
        if(ObjectUtil.isNull(categoryEntity)) return this.setResultError("数据不存在");

        //判断是否为父节点
        if (categoryEntity.getIsParent() == 1) return this.setResultError("当前为父节点 不能删除");

        //通过当前父节点id查询 当前父节点下是否还有其他子节点
        Example example = new Example(CategoryEntity.class);
        example.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        List<CategoryEntity> list = mapper.selectByExample(example);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("parentId",categoryEntity.getParentId());


        //判断是否有品牌类的关系分类表
        Example example1 = new Example(BrandCategoryEntity.class);
        example1.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        List<BrandCategoryEntity> brandCategoryEntities = categoryBrandMapper.selectByExample(example1);
        if (brandCategoryEntities.size()>0) return this.setResultError("当前节点绑定品牌,请先删除品牌后再删该节点");


        //根据删除的parentId为节点 查询父级节点的状态
        if (list.size() <=1){
            CategoryEntity UpdateCategoryEntity = new CategoryEntity();
            UpdateCategoryEntity.setParentId(0);
            UpdateCategoryEntity.setId(categoryEntity.getParentId());

            mapper.updateByPrimaryKey(UpdateCategoryEntity);
        }
        mapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JsonObject> editCategory(CategoryEntity categoryEntity) {
        mapper.updateByPrimaryKeySelective(categoryEntity);
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JsonObject> saveCategory(CategoryEntity categoryEntity) {

        //新增一个节点的时候 在新增的节点下再新增一个节点的时候把节点改为父节点
        CategoryEntity parentCategoryEntity = new CategoryEntity();
        parentCategoryEntity.setId(categoryEntity.getParentId());
        parentCategoryEntity.setIsParent(1);
        mapper.updateByPrimaryKeySelective(parentCategoryEntity);

        mapper.insertSelective(categoryEntity);
        return this.setResultSuccess();
    }

    @Override
    public Result<List<CategoryEntity>> getBrandById(Integer brandId) {
        List<CategoryEntity> byBrandId = mapper.getByBrandId(brandId);
        return this.setResultSuccess(byBrandId);
    }


}
