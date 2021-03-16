package com.baidu.shop.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016110200786810";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCHzvagbzUQssi1kvcRvcqSawz6YKKmQQ1l++50LZrwnfqbmlI8g7Z4seYtmhrMekn6jbwJhh7uXb1fKwFg9C1ITNV2Dlehy2G3mJv6MfWEyI9eH+ho4ElFch3+SlObii1aDxjR0es4qi4xWExs1hkUm93Hc1AuL2+8khRLI8z1eTYlLbVubUY4zT82WSyuNuVYID1VO9CdQ6lPCAHBWSaj7JZdDEHihDxLZXy3YLy4C2M9chZ+crT7Js9SrFm/PhWxxZSpl/8Jy2WK5pWTcSMa9h2XtsVz7EkqlDsVXlyZZYNTZSYls37ovLpojgqPRnI/HlQ+GfGqww2jcncIW/4FAgMBAAECggEBAIActbysi3OAJCRdSiL7Vd//ilOa23RYQ4pkORJxfwrjDGjDEDazzWtX7powGH2yEp2AHNUjJ4SW+zCOghNy5FCi4m8monYnRLwMA3/mpwQmqlARa42bnqMbPr4GYY6Fr7iYK0KrcuiWaVL1R8sXwLsXOgSPfCDFcPODHg+v6wXYeZA4Ic/nfHCxGxKygYRWcA6RyHS9TWALvR3e+ZoFdxI1lHXqWh4kL3HvsWQsaBpeDMVGqeJGwNuadYaen753Pjapgq23WeMyZ4M6cPeXMKT96jFov69mV2FaAOJxrGQsOYuk/e8gQV1nl3PacEenIZniQWJbFDLrytRdALFObnUCgYEA2GRRtDLahfklPKXdOIQ9V9mh455lCiCHDyzNQQBXpEXw26nXilF/vDEZP3fAgd+g4W9fjGs9v895Bm0QJjM2dMDUiX7T5bNsSl/rO09+95h2OE8Pn7Eka/vQ44l27ywcNCOuWHeHPmWnap7KPQwM8BADGUWfdcTCuxQ8C+qFpN8CgYEAoKqreVAsOeTOzoCTp7ZHkJQ6xHi2+R20IzOkM7Q8N8d+ZCM3f+QB32dqn5xl/vLomYBxKU2zuriTAE0cNMBpYhVablsrF4iqRJlB0iJ8uvCdcJsKIlxQr97eKlsCuYHnv3IKQG1UmCRnHnTQ0SKtzrgnFJL3t7iqz435o4SRNZsCgYAMAWTXDos5K515Q+qGHDAbE6P7sOoCKdPUb7Pji1VRc9/tD/s5XVQs6Lxx3P79NZ8DYt52N+zxG12nkZ13wRegWqJwDm1LLS0w4yl13O86ZWcJsLk5LubKZuor9ZB3rIbUDba6iccjGxiyvaSSxnxhRUImwMocCpKTNoguURChGQKBgQCVzIr0IPwbPmpTvy3sQNeYxZvFc4el3TwHVi7Sxkke+LGvS+aDJ2NXmGShTIEMpA6akFbx27Cnd1AYB800ofGfsakPG5Gh75pRO1NTSTRiV9VzGBrqzoescm9Rrmlr7OLWbM2CJG2rLMW4b9ICLANBqhFF7YFpmpQn0IfhHckF4QKBgQC9wSJ4zWDmM4/GzypnIddtBsZejYRzHHw+197zRI9yA1GZlCL7SCKAp8gR03yiofvYrNOp0Pz3Qi8DZhrAUB7NetU4ZswCXvMLtCpl65voFLKJKupZift0KbgaeJGAjCBFnMBvcBudUZXCEHlbqrpKKvoopARb1RZWNRf1nVAmEQ==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmEa5nc0wTcC/irXUnUxrZpNJEknUAo8AKI4mI5my8vS7ls2fnVh8QYj70aFkgB7Nr5OqMvQnTNpG1vp0UgjSIPNCDxVIbM3chLV5FFve6kKTtyNiCdnIGCQJsxWQsGD+NL1dHfMApN9yBqRvt1/KbMM9Zx3+YMnbbrBXp+tkdrZOvE/2Z8Sb3ljVMAcRs+uJiuAwOjxDsnkJzV16w30qPVcmIkK3zB85aoZ+NnGVsCwcxMq/zpRLMBEyocjO3q27hRt1aNe0/dotORV8ve1L/EIigVuHa2gusCGPrxCdn3AMn8w7qPw+7qxIiO7BKz6HaIbG+2Vz5skRhMPdEr9jkQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8900/pay/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8900/pay/return";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "D:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

