package com.baidu.shop.web;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.business.UserOauthService;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.UserEntity;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.CookieUtils;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utils.ObjectUtil;
import com.baidu.shop.utils.ObjectUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "oauth")
public class UserOauthController extends BaseApiService {

    @Resource
    private UserOauthService userOauthService;

    @Autowired
    private JwtConfig jwtConfig;


    @GetMapping(value = "verify")
    public Result<UserInfo> checkUserIsLogin(@CookieValue(value = "MRSHOP_TOKEN") String token,HttpServletRequest request,HttpServletResponse response){

        UserInfo userInfo = null;
        try {
            userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            String newToken = JwtUtils.generateToken(userInfo,jwtConfig.getPrivateKey(),jwtConfig.getExpire());

            //将新token 写入cookie过期时间延长了
            CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),newToken,jwtConfig.getCookieMaxAge(),true);
        } catch (Exception e) {//如果有异常就说明token有问题
            e.printStackTrace();
            return this.setResultError(HTTPStatus.USER_NOT_LOGIN,"用户没有登陆");
        }
        return this.setResultSuccess(userInfo);
    }


    @PostMapping(value = "login")
    public Result<JSONObject> login(@RequestBody UserEntity userEntity, HttpServletRequest request, HttpServletResponse response){

        String token = userOauthService.login(userEntity, jwtConfig);

        if (ObjectUtil.isNull(token)) return this.setResultError("账号或密码不正确");
        //将token放入cookie中
        CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),token
                , jwtConfig.getCookieMaxAge(),true);

        return this.setResultSuccess();
    }



}
