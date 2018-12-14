package com.project.controller.console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.common.ServerResponse;
import com.project.common.wxpay.ConstantUtil;
import com.project.common.wxpay.Md5Util;
import com.project.common.wxpay.PrepayIdRequestHandler;
import com.project.common.wxpay.WXUtil;
import com.project.common.wxpay.WxNotifyParam;
import com.project.common.wxpay.XMLUtil;
import com.project.pojo.VMember;
import com.project.pojo.VOrder;
import com.project.pojo.VUser;
import com.project.service.VMemberService;
import com.project.service.VOrderService;
import com.project.service.VUserService;
import com.project.util.DateTimeUtil;

@CrossOrigin
@Controller
@RequestMapping("/wxpay")
public class Wxpay {
	
	@Autowired
	private VOrderService vordersService;
	
	@Autowired
	private VUserService vuserService;
	
	@Autowired
	private VMemberService vmemberService;
	
	/**
	 * 初始化微信支付
	 * 
	 * @param request
	 * @param response
	 * @param orderId
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value = "/pay/{num}/{price}/{type}/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<WxNotifyParam> initWx(@PathVariable("num") Integer num,@PathVariable("uid") Integer uid,@PathVariable("price") BigDecimal price,@PathVariable("type") Integer type,
			HttpServletRequest request, HttpServletResponse response) {
		
		Date date = new Date();
		
		String time = DateTimeUtil.getSecondTimestamp(date);
		int i = (int)(Math.random()*100+1);
		String string = time+""+i;
		BigDecimal answer= price.multiply(new BigDecimal(num));
		BigDecimal payment= answer.multiply(new BigDecimal(100));
		int p = payment.intValue();
		
		VOrder order = new VOrder();
		order.setCreateTime(date);
		order.setNum(num);
		order.setOrderNumber(string);
		order.setPaymentType(0);
		order.setPrice(answer);
		order.setType(type);
		order.setUid(uid);
		
		
		// 修改订单支付类型
		//ordersMapper.updatePayType(Orders.PaymentType.WX.toString(), orderId);
		//Double payment = userOrder.getPayment().doubleValue();
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取生成预支付订单的请求类
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);
		String testPrice = String.valueOf(p);
		//String totalFee = String.valueOf(payment);//微信支付是 分--
		// 上线后，将此代码放开
		prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
		prepayReqHandler.setParameter("body", ConstantUtil.BODY);
		prepayReqHandler.setParameter("mch_id", ConstantUtil.MCH_ID);
		String nonce_str = WXUtil.getNonceStr();
		prepayReqHandler.setParameter("nonce_str", nonce_str);
		prepayReqHandler.setParameter("notify_url", ConstantUtil.NOTIFY_URL);
		String out_trade_no = string;
		prepayReqHandler.setParameter("out_trade_no", out_trade_no);
		// "14.23.150.211"
		prepayReqHandler.setParameter("spbill_create_ip", request.getRemoteAddr());
		String timestamp = WXUtil.getTimeStamp();
		prepayReqHandler.setParameter("time_start", timestamp);
		// System.out.println("金额" + totalFee);
		prepayReqHandler.setParameter("total_fee", testPrice);
		prepayReqHandler.setParameter("trade_type", "APP");
		/**
		 * 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
		 */
		prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign());
		prepayReqHandler.setGateUrl(ConstantUtil.GATEURL);
		String prepayid;
		WxNotifyParam param = new WxNotifyParam();
		try {
			prepayid = prepayReqHandler.sendPrepay();
			// 若获取prepayid成功，将相关信息返回客户端
			if (prepayid != null && !prepayid.equals("")) {
				String signs = "appid=" + ConstantUtil.APP_ID + "&noncestr=" + nonce_str
						+ "&package=Sign=WXPay&partnerid=" + ConstantUtil.PARTNER_ID + "&prepayid=" + prepayid
						+ "&timestamp=" + timestamp + "&key=" + ConstantUtil.APP_KEY;
				/**
				 * 签名方式与上面类似
				 */
				param.setPrepayId(prepayid);
				param.setSign(Md5Util.MD5Encode(signs, "utf8").toUpperCase());
				param.setAppId(ConstantUtil.APP_ID);
				// 等于请求prepayId时的time_start
				param.setTimeStamp(timestamp);
				// 与请求prepayId时值一致
				param.setNonceStr(nonce_str);
				// 固定常量
				param.setPackages("Sign=WXPay");
				param.setPartnerId(ConstantUtil.PARTNER_ID);
				vordersService.insert(order);
				System.out.println("-----------》创建微信订单成功: " + param);
				return ServerResponse.createBySuccess("创建订单成功", param);
			} else {
				return ServerResponse.createByErrorCodeMessage(453, "获取微信prepayid失败");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return ServerResponse.createByErrorCodeMessage(454, "创建微信支付异常");
		}
	}

	/**
	 * 接收微信支付回调通知
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */

	@RequestMapping(value = "/notify_url", method = RequestMethod.POST)
	public void getTenPayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入微信回调方法");
		PrintWriter writer = response.getWriter();
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");
		System.out.println("微信回调支付通知结果：" + result);
		Map<String, String> map = null;
		try {
			/**
			 * 解析微信通知返回的信息
			 */
			map = XMLUtil.doXMLParse(result);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("- - - - - - - - -");
		System.out.println("= = = = = = = = =:" + map);
		// 若支付成功，则告知微信服务器收到通知
		if (map.get("return_code").equals("SUCCESS")) {
			if (map.get("result_code").equals("SUCCESS")) {
				System.out.println("微信充值会员体系成功！");
				String out_trade_no = map.get("out_trade_no");

				//System.out.println("success");
				//判断订单付款是否成功，以防多次回调
				VOrder Order = vordersService.selectByPrimaryKey(out_trade_no);
				if(Order != null){
					if (Order.getState().equals("1")) {
						// 处理会员体系业务逻辑
						return;
					}
					
					VOrder vOrder = vordersService.selectByPrimaryKey(out_trade_no);
					VUser u = (VUser) vuserService.selectById(vOrder.getUid());
					Date date = null;
					Date d = new Date();
					Integer flag = 0;
					if(u.getExpireTime()==null){
						date = new Date();
						flag = 1;
					}else if((u.getExpireTime().getTime()-d.getTime())<0){
						date = new Date();
						flag = 2;
					}else if((u.getExpireTime().getTime()-d.getTime())>=0){
						date = u.getExpireTime();
						flag = 2;
					}
					
					
					Integer type = vOrder.getType();
					Integer num = vOrder.getNum();
					String expireTime = null;
					Integer maxlogin = 0;
					Integer ismember = 1;
					//1天  2月  3季 4半年 5年
					if (type == 1) {
						expireTime = DateTimeUtil.addOneDay(date,num);
						VMember vm = vmemberService.selectByType(type);
						maxlogin = vm.getMaxlogin();
					} else if (type == 2){
						expireTime = DateTimeUtil.addOneMonth(date,num);
						VMember vm = vmemberService.selectByType(type);
						maxlogin = vm.getMaxlogin();
					}else if (type == 3){
						expireTime = DateTimeUtil.addThreeMonth(date,num);
						VMember vm = vmemberService.selectByType(type);
						maxlogin = vm.getMaxlogin();
					}else if (type == 4){
						expireTime = DateTimeUtil.addSixMonth(date,num);
						VMember vm = vmemberService.selectByType(type);
						maxlogin = vm.getMaxlogin();
					}else if (type == 5){
						expireTime = DateTimeUtil.addOneYear(date,num);
						VMember vm = vmemberService.selectByType(type);
						maxlogin = vm.getMaxlogin();
					}else if (type == 6){
						expireTime = DateTimeUtil.addOneYear(date,100);
						VMember vm = vmemberService.selectByType(type);
						maxlogin = vm.getMaxlogin();
						ismember = 2;
					}
					
					if(flag!=2){
						vuserService.updateByUid(vOrder.getUid(),date,expireTime,maxlogin,ismember);
					}else{
						vuserService.updateByUid2(vOrder.getUid(),expireTime,maxlogin,ismember);
					}

					vordersService.updateByOrderNum(out_trade_no);
					
				}else {
					System.out.println("微信支付回调订单不存在");
				}
			} else {
				System.out.println("微信支付回调失败");
			}
		}
	}
}
