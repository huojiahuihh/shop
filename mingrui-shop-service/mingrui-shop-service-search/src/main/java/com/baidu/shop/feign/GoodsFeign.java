package com.baidu.shop.feign;

import com.baidu.shop.service.GoodsService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "GoodsService",value = "xxx-server")
public interface GoodsFeign extends GoodsService {
}
