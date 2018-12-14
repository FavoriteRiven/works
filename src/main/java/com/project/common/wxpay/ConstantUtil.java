package com.project.common.wxpay;


public class ConstantUtil {
	/**
	 * 微信开发平台应用ID
	 */
	public static final String APP_ID = "wx73dae845426a59bf";
	/**
	 * 应用对应的AppSecret
	 */
	public static final String APP_SECRET = "1ee1298cddc82629609cafc496bcc155";
	/**
	 * APP_KEY 商户平台---api安全---密钥
	 */
	public static final String APP_KEY = "xiluwangluokeji00100200300500607";
	/**
	 * 微信支付商户号
	 */
	public static final String MCH_ID = "1509659971";
	/**
	 * 商品描述
	 */
	public static final String BODY = "棉花代理";
	/**
	 * 商户号对应的密钥 也是 商户平台---api安全---密钥 同上面那个APP_KEY
	 */
	public static final String PARTNER_key = "xiluwangluokeji00100200300500607";

	/**
	 * 商户id 同微信支付商户号
	 */
	public static final String PARTNER_ID = "1509659971";
	/**
	 * 常量固定值
	 */
	public static final String GRANT_TYPE = "client_credential";
	/**
	 * 获取预支付id的接口url
	 */
	public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 微信服务器回调通知url
	 */
	//测试
	//public static String NOTIFY_URL = "192.168.31.88:808/timepresent/wx/notify_url";
	//服务器
	public static String NOTIFY_URL = "http://116.255.190.125:8080/works/wxpay/notify_url";

}