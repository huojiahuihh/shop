package com.baidu.feign;

import com.baidu.shop.service.SpecificationService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "SpecificationService",value = "xxx-server")
public interface SpecificationFeign extends SpecificationService {
}
