package com.baidu.shop.business;

import com.baidu.shop.dto.PayInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "支付接口")
public interface PayService {
    @ApiOperation(value="请求支付")
    @GetMapping(value = "pay/requestPay")
    void requestPay(PayInfoDTO payInfoDTO, @CookieValue(value = "MRSHOP_TOKEN") String token, HttpServletResponse response);

    @ApiOperation(value ="通知接口,这个接口用不了")
    @GetMapping(value = "pay/notify")
    void notify(HttpServletRequest request);

    @ApiOperation(value ="跳转成功 页面接口")
    @GetMapping(value = "pay/return")
    void returnUrl(HttpServletRequest httpServletRequest, HttpServletResponse response);
}
