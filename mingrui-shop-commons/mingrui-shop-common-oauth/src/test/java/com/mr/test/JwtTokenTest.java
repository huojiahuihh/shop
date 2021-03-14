package com.mr.test;

import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTokenTest {
    //公钥位置
    private static final String pubKeyPath = "D:\\rea.pub";
    //私钥位置
    private static final String priKeyPath = "D:\\rea.pri";
    //公钥对象
    private PublicKey publicKey;
    //私钥对象
    private PrivateKey privateKey;
    /**
     * 生成公钥私钥 根据密文
     * @throws Exception
     */
    @Test
    public void genRsaKey() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "mingrui");
    }

    /**
     * 从文件中读取公钥私钥
     * @throws Exception
     */
    @Before
    public void getKeyByRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }
    /**
     * 根据用户信息结合私钥生成token
     * @throws Exception
     */
    @Test
    public void genToken() throws Exception {
    // 生成token
        String token = JwtUtils.generateToken(new UserInfo(1, "huojiahui"), privateKey, 2);
        System.out.println("user-token = " + token);
    }
    /**
     * 结合公钥解析token
     * @throws Exception
     */
    @Test
    public void parseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJodW9qaWFodWkiLCJleHAiOjE2MTU1Mjk4NTF9.GB2XsZstZo-ZYPohTZd98XWVZWCWhDDK6Hz3m4vM8A38LTfZcjLMvX0H6-7rEdyDSXfwQYZfYsPQxjO1o_eCoYpIFt9dbpXj14IIHzzw9Y8G_RHJZKEZyeOWbvqbKjS8epIC5oKcUQO-PHsRlU5ZW1muzv4MykiM1csT-Jq7Cgg";
// 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }

}
