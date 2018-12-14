package com.project.util;

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
	public static String app_id = "2018071160524909";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDlPQ8J86ScsksjOgfxtMANAnmXyA+jTZ3nQlpnTlMl2BzBgR41rZWP0J02rCrpdrWvF6HNI+VnYP3CVABIQ1aJz2ajt2afLubtsidcInUtrn8yWC7pjlawpUNyo1w6+Ofp3smq+ZXVoxApWQ888JFSgmLsbRoqp6dKCiLiXXEtPpLPU/Opq2y9p6QE5V6o8eLY6lz4AWh+//tFvWJWIq057RRflr9olIgTUAx8tIGXCqB67ETRfQNY7BbWtn7drr5fapD8ImSyKSl254wuZ/M6/aLrWyrGUAIduIc9U/hqTOImtO34eLSaD3e4gMklERmdqWT1O1pRyopTO3wLOEYfAgMBAAECggEBAMMlHV5NaL50EP6l8glS44Kvrq8FAjr0Yg1yMN9sj9/Rf3lC32K2qHFLF+po2gbwpW2uYnDts+YzbRI2zB64JD0gr7GC+Ptdw/xpApSuSK5gs6s54oW0RBwQM8CtTGBvb41puKex3FmHfMSWKiCoci2ZDXlSz0msev7Obi/hd+0YkPUSKBNEoUngUpFCcll7hfj0pZnhpHSiESZ4U1Q0tIHdVboZJQwJykeUK4cHo40vdhPjrLD+uwu7dR3MlTcg6+HZJN97uMzGk5fW64HlhY57duJcJz0FFVC5oQXoqaI3zQnjPzIB7bOMs4cnhKe6Mt+omLHqBhx/lbKJUCtzJ7ECgYEA+A+Hwo79DBA2YTryh+kiaBQr2d1Mt4EPU2tDhIfOtw4lwxHK62Ep2iZulabBGOHGNZZDTFcZf09rAYJEP5o0OCT2hmoUpZSTz6DSm07eifobyisIIwEDu/4g5SGmbvVgh82aHgEmcLpli+NUBzBl1nWWR17jCwm9Q4FT2bTu0fUCgYEA7JNPbkOi1tnVrTOE7wOom22hdE7h0IFklX8U/aO1ZLfCnuZ70MZYlJJH1zzLjwKeKlySSB/il+BS0Rmqz6j836hF4q7ngOjj7yhhl6zwD7qo8Prl6hhef5n+k2rcXuN0+B2wlJoMLf0Aa9aE40qCX/SaDnqpuvcVCiqzED/vJ0MCgYEA1Ay6mfePmztUsSMp5kLF1cDAh5nklc1uo3nDGszZSaR/ZVSVaUcC9uf5ilYlJTcDMBXGa8DdG/Zw27ZWzyPr1b7LOrX99lmfTVMGFhuCouI51UWZzAp2/cJMrnJoituZbB22zEVhbeX5Hm7zCr41n/GeyF31qh7mOomYezdvG+0CgYB4LIB955exgihb+j6912LvyWnpliR5cTEyC8MS8PU0+2++niApNohKuGAMAxL4S4EizbF1N9U4BziqhVpScGo8BtOxSd3JiwQ2OZIRpUSxCMXG2/ysxMJEGWrLYc4d2XdSjmpjojkrp0hufyMz/mZp7evQDRlpIxgp+PTK/V8z1wKBgCtYRNb4o9RX23BMPNDvYHlL81IsO3+D8fxtZhGYLAfM4vf1kGwvyDyRx2LO88JZjOFOb9Nsd/TH83Vv+o9PgxVz07fZSIVCQzlPZ0c7LQA9nQC9pLOJOL3UhIHr/abBtC3SdQya7rIaL+Uwwy5Z6Sl2AuG1ayzNhh5FsUJ4BWtT";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhft1h5ULobG0DbHQtoJ1YS7ZRTQgC+RmB2EGVnduDhvu03oGuug4f43UaOQWd2BEnVRmW52jnceBpfeOIbiBuXBVFjRdEGwMmMPBK+MECF1le65Wtp5mEqSOkJu+R//OZg34QtfxjhdEQCUYnmyIJa7KST7v0ThLllw/4BFiXWl0niXDal5VVgoIXpvEq6y1MTiLMcRsd3xbHotS4TWrB8G+9kCJNakYp0kHfZcK8qClTr0+I4glCWFHi9nChS07HQRJSPOHzkoDEo+XKf+eb2kj4+KtPAar17PT6s83ptW2hL2UrcbuRkLlsgkIZiOJPlN/T9Cro58jXhBoZSMBbQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://116.255.190.125:8080/works/pay/notify_url";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://116.255.190.125:8080/works/pay/notify_url";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


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

