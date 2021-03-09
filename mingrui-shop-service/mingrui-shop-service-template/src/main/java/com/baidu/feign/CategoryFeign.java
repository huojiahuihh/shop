package com.baidu.feign;

import com.baidu.shop.service.CategoryService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "categoryService",value = "xxx-server")
public interface CategoryFeign extends CategoryService {
}
