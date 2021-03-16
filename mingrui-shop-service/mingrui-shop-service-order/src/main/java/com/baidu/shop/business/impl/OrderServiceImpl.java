package com.baidu.shop.business.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.business.OrderService;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.dto.Car;
import com.baidu.shop.dto.OrderDTO;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.OrderDetailEntity;
import com.baidu.shop.entity.OrderEntity;
import com.baidu.shop.entity.OrderStatusEntity;
import com.baidu.shop.mapper.OrderDetailMapper;
import com.baidu.shop.mapper.OrderMapper;
import com.baidu.shop.mapper.OrderStatusMapper;
import com.baidu.shop.redis.repository.RedisRepository;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.IdWorker;
import com.baidu.shop.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class OrderServiceImpl extends BaseApiService implements OrderService {


    public static String GOODS_CAR_PRE = "goods_car_";
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDetailMapper orderDetailMapper;
    @Resource
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisRepository redisRepository;

    @Override
    @Transactional
    public Result<String> creatOrder(OrderDTO orderDTO, String token) {

        long orderId = idWorker.nextId();
        log.info("orderId: {}",orderId);
        Date date = new Date();
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //准备orderEntity的数据
            OrderEntity orderEntity = new OrderEntity();

            orderEntity.setOrderId(orderId);
//            orderEntity.setTotalPay( );
//            orderEntity.getActualPay();
            orderEntity.setPromotionIds("1,2");
            orderEntity.setPaymentType(1);
            orderEntity.setCreateTime(date);
            orderEntity.setUserId(userInfo.getId() +" ");
            orderEntity.setBuyerMessage("差不多");
            orderEntity.setBuyerNick("杨凯");
            orderEntity.setBuyerRate(2);
            orderEntity.setInvoiceType(1);
            orderEntity.setSourceType(1);
            List<Long> priceList = new ArrayList<>();

            //orderDetailEntity 的数据准备
            List<OrderDetailEntity> orderDetailEntityList = Arrays.asList(orderDTO.getSkuIds().split(",")).stream().map(skuIdStr ->{
                Car redisCar = redisRepository.getHash(GOODS_CAR_PRE + userInfo.getId(), skuIdStr, Car.class);

                OrderDetailEntity orderDetailEntity = BaiduBeanUtil.copyProperties(redisCar, OrderDetailEntity.class);
                orderDetailEntity.setOrderId(orderId);
                priceList.add(orderDetailEntity.getPrice() * orderDetailEntity.getNum());
                return orderDetailEntity;
            }).collect(Collectors.toList());

            Long totalPrice = priceList.stream().reduce(0L, (oldVal, currentVal) -> oldVal + currentVal);
            orderEntity.setTotalPay(totalPrice);
            orderEntity.setActualPay(totalPrice);

            //orderStatusMapper 的数据准备
            OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
            orderStatusEntity.setOrderId(orderId);
            orderStatusEntity.setCreateTime(date);
            orderStatusEntity.setStatus(1);

            //入库
            orderMapper.insertSelective(orderEntity);
            orderDetailMapper.insertList(orderDetailEntityList);
            orderStatusMapper.insertSelective(orderStatusEntity);

            //将的当前商品从购物车中删掉
            Arrays.asList(orderDTO.getSkuIds().split(",")).stream().forEach(skuIdStr -> {
                redisRepository.delHash(GOODS_CAR_PRE + userInfo.getId(), skuIdStr);
            });
        } catch (Exception e) {
            e.printStackTrace();
            this.setResultError("用户实效");
        }
        return this.setResult(HTTPStatus.OK,"",orderId +"");
    }
}