package com.project.controller.portal;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.project.common.ServerResponse;
import com.project.pojo.VMember;
import com.project.pojo.VOrder;
import com.project.pojo.VUser;
import com.project.service.VMemberService;
import com.project.service.VOrderService;
import com.project.service.VUserService;
import com.project.util.AlipayConfig;
import com.project.util.DateTimeUtil;
@CrossOrigin
@Controller
@RequestMapping("/pay")
public class PayController {
	
	
	@Autowired
	private VOrderService vordersService;
	
	@Autowired
	private VUserService vuserService;
	
	@Autowired
	private VMemberService vmemberService;
	
	/**
	 * 执行支付宝支付
	 * @param orderId
	 * @return
	 * @throws AlipayApiException
	 */
	@RequestMapping(value = "/alipay/{num}/{price}/{type}/{uid}", produces = "application/json; charset=utf-8")
	@ResponseBody
	public ServerResponse<String> alipay(@PathVariable("num") Integer num,@PathVariable("uid") Integer uid,@PathVariable("price") BigDecimal price,@PathVariable("type") Integer type){
		
		Date date = new Date();
		
		String time = DateTimeUtil.getSecondTimestamp(date);
		int i = (int)(Math.random()*100+1);
		String string = time+""+i;
		BigDecimal answer= price.multiply(new BigDecimal(num));
		VOrder order = new VOrder();
		order.setCreateTime(date);
		order.setNum(num);
		order.setOrderNumber(string);
		order.setPaymentType(0);
		order.setPrice(answer);
		order.setType(type);
		order.setUid(uid);
		int p = answer.intValue();
		
		
		String bodys="棉花代理平台"+string+"订单的商品收款";
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(bodys);
		model.setSubject(string+"收款"); 
		model.setOutTradeNo(string);
		 
		model.setTimeoutExpress("30m");
		model.setTotalAmount(String.valueOf(p)+"");
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(AlipayConfig.notify_url);
		try {
		        //这里和普通的接口调用不同，使用的是sdkExecute
		        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
		       // System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
		        vordersService.insert(order);
				return ServerResponse.createBySuccess("处理成功",response.getBody());
				
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		}
		return ServerResponse.createByErrorCodeMessage(2,"处理失败");
	}
	
	
	/**
	 * 测试回调
	 * @param out_trade_no
	 * @return
	 */
	@RequestMapping(value="alipay/test/{out_trade_no}",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public ServerResponse<String> test(@PathVariable("out_trade_no")String out_trade_no){
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
		//System.out.println("success");
		return null;
	}
	
	
	
	/**
	 * 支付回调
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "notify_url", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
 
		// 1.从支付宝回调的request域中取值
		Map<String, String[]> requestParams = request.getParameterMap();

		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = iter.next();
			String[] values = requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		// 2.封装必须参数
		// 商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
		// 支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
		String tradeStatus = request.getParameter("trade_status"); // 交易状态

//		System.out.println(out_trade_no);
//		System.out.println(trade_no);
//		System.out.println(tradeStatus);
		// 3.签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
		boolean signVerified = false;
		try {
			signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
					AlipayConfig.sign_type);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		} // 调用SDK验证签名
			// 4.对验签进行处理
		if (signVerified) { // 验签通过
			if (tradeStatus.equals("TRADE_SUCCESS")) { // 只处理支付成功的订单: 修改交易表状态,支付成功
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("orderCode",out_trade_no );
				
				VOrder vOrder = vordersService.selectByPrimaryKey(out_trade_no);
				
				if(vOrder!=null){
					if (vOrder.getState().equals("1")) {
						// 处理会员体系业务逻辑
						return "fail";
					}
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
				}
				
				//System.out.println("success");
				return "success";
			} else {
				//System.out.println("没有支付成功的订单");
				return "fail";
			}
		} else { // 验签不通过
			//System.err.println("验签失败");
			return "fail";
		}
	}

}
