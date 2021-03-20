package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.dto.Car;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.SkuEntity;
import com.baidu.shop.feign.GoodsFeign;
import com.baidu.shop.redis.repository.RedisRepository;
import com.baidu.shop.service.CarService;
import com.baidu.shop.utils.JSONUtil;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
@Slf4j
public class CarServiceImpl extends BaseApiService implements CarService {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private GoodsFeign goodsFeign;

    public static String GOODS_CAR_PRE = "goods_car_";

    @Override
    public Result<JSONObject> addCar(Car car, String token) {
        //购物车
        ArrayList<Object> objects = new ArrayList<>();
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //通过用户和商品id从购物车中获取数据
            //通过用户id和sku查询redis数据
            Car redisCar = redisRepository.getHash(GOODS_CAR_PRE + userInfo.getId(), car.getSkuId() + "", Car.class);
            objects.add(redisCar);
            log.info("通过用户id : {} ,skuId : {} 从redis中获取到的数据为 : {}",userInfo.getId(),car.getSkuId(),redisCar);
            if (ObjectUtil.isNotNull(redisCar)){
                //如果查询出来
                redisCar.setNum(redisCar.getNum() + car.getNum());
                redisRepository.setHash(GOODS_CAR_PRE + userInfo.getId(),car.getSkuId() + "", JSONUtil.toJsonString(redisCar));
                log.info("redis中有当前商品 , 重新设置redis中该商品的数量 : {}",redisCar.getNum());
            }else{
                //查询不出来
                Result<SkuEntity> skuResult = goodsFeign.getSkuById(car.getSkuId());
                if (skuResult.isSuccess()) {
                    //如果不能查询出来数据
                    //通过skuId查询sku信息
                    //往redis中新增一个商品
                    SkuEntity skuEntity = skuResult.getData();
                    car.setTitle(skuEntity.getTitle());
                    car.setImage(StringUtils.isEmpty(skuEntity.getImages()) ? "" : skuEntity.getImages().split(",")[0]);
                    car.setPrice(skuEntity.getPrice().longValue());
                    //skuEntity.getOwnSpec() 转换成map --> 遍历map --> 通过key查询spec_param表中的name值 --> 重新拼一个map集合 --> 将map集合转string字符串
                    car.setOwnSpec(skuEntity.getOwnSpec());

                    redisRepository.setHash(GOODS_CAR_PRE + userInfo.getId(),car.getSkuId() + "",JSONUtil.toJsonString(car));
                    log.info("redis中没有当前商品 , 新增商品到购物车中 userId : {} , skuId : {} ,商品数据 : {}",userInfo.getId(),car.getSkuId(),JSONUtil.toJsonString(car));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.setResultSuccess(objects);
    }

    @Override
    public Result<JSONObject> mergeCar(String carList, String token) {
        //将string类型的字符串({carList:[]})转换成json对象
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(carList);
        //通过key获取数据
        JSONArray carListJsonArray = jsonObject.getJSONArray("carList");
        //将json数组转换成集合
        List<Car> carsList = carListJsonArray.toJavaList(Car.class);

        carsList.stream().forEach(car -> {
            this.addCar(car,token);
        });
        return this.setResultSuccess();
    }

    @Override
    public Result<List<Car>> getUserCar(String token) {
        List<Car> cars = new ArrayList<>();
        try {
            //获取当前登录用户
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //通过用户id从redis获取购物车数据
            Map<String, String> map = redisRepository.getHash(GOODS_CAR_PRE + userInfo.getId());
            map.forEach((key,value) -> cars.add(JSONUtil.toBean(value,Car.class)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.setResultSuccess(cars);
    }

    @Override
    public Result<JSONObject> operationNum(String token, Integer type,Long skuId) {

        try {
            //获取当前登录用户
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            Car redisCar = redisRepository.getHash(GOODS_CAR_PRE + userInfo.getId(), skuId + "", Car.class);
            if (type ==1){
                redisCar.setNum(redisCar.getNum() + 1);
            }else {
                redisCar.setNum(redisCar.getNum() - 1);
            }
            redisRepository.setHash(GOODS_CAR_PRE +userInfo.getId(),skuId + "",JSONUtil.toJsonString(redisCar));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.setResultSuccess();
    }


}
