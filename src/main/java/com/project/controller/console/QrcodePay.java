package com.project.controller.console;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.MonitorHeartbeatSynResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.*;
import com.alipay.demo.trade.model.hb.*;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.Utils;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.project.common.ServerResponse;
import com.project.pojo.VMember;
import com.project.pojo.VOrder;
import com.project.pojo.VUser;
import com.project.service.VMemberService;
import com.project.service.VOrderService;
import com.project.service.VUserService;
import com.project.util.AlipayConfig;
import com.project.util.DateTimeUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liuyangkly on 15/8/9.
 * 简单main函数，用于测试当面付api
 * sdk和demo的意见和问题反馈请联系：liuyang.kly@alipay.com
 */
@CrossOrigin
@Controller
@RequestMapping("/pcpay")
public class QrcodePay {
    private static Log                  log = LogFactory.getLog(QrcodePay.class);

    // 支付宝当面付2.0服务
    private static AlipayTradeService   tradeService;

    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
    private static AlipayTradeService   tradeWithHBService;

    // 支付宝交易保障接口服务，供测试接口api使用，请先阅读readme.txt
    private static AlipayMonitorService monitorService;
    
    @Autowired
	private VOrderService vordersService;
	
	@Autowired
	private VUserService vuserService;
	
	@Autowired
	private VMemberService vmemberService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
            .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
            .setFormat("json").build();
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                    response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    public static void main(String[] args) {
        QrcodePay main = new QrcodePay();

        // 系统商商测试交易保障接口api
                //main.test_monitor_sys();

        // POS厂商测试交易保障接口api
               //main.test_monitor_pos();

        // 测试交易保障接口调度
        //        main.test_monitor_schedule_logic();

        // 测试当面付2.0支付（使用未集成交易保障接口的当面付2.0服务）
        //        main.test_trade_pay(tradeService);

        // 测试查询当面付2.0交易
        //        main.test_trade_query();

        // 测试当面付2.0退货
        //        main.test_trade_refund();

        // 测试当面付2.0生成支付二维码
        //main.test_trade_precreate();
        
    }

    // 测试系统商交易保障调度
    /*public void test_monitor_schedule_logic() {
        // 启动交易保障线程
        DemoHbRunner demoRunner = new DemoHbRunner(monitorService);
        demoRunner.setDelay(5); // 设置启动后延迟5秒开始调度，不设置则默认3秒
        demoRunner.setDuration(10); // 设置间隔10秒进行调度，不设置则默认15 * 60秒
        demoRunner.schedule();

        // 启动当面付，此处每隔5秒调用一次支付接口，并且当随机数为0时交易保障线程退出
        while (Math.random() != 0) {
            test_trade_pay(tradeWithHBService);
            Utils.sleep(5 * 1000);
        }

        // 满足退出条件后可以调用shutdown优雅安全退出
        demoRunner.shutdown();
    }*/



    // 测试当面付2.0生成支付二维码
    @RequestMapping(value = "/alipay/{num}/{price}/{type}/{uid}", produces = "application/json; charset=utf-8")
	@ResponseBody
	public ServerResponse<String> alipay(@PathVariable("num") Integer num,@PathVariable("uid") Integer uid,@PathVariable("price") BigDecimal price,@PathVariable("type") Integer type,HttpServletRequest request){
		
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
    	Date date = new Date();
    	String time = DateTimeUtil.getSecondTimestamp(date);
		int i = (int)(Math.random()*100+1);
        String outTradeNo = time+""+i;
        
        BigDecimal answer= price.multiply(new BigDecimal(num));
		VOrder order = new VOrder();
		order.setCreateTime(date);
		order.setNum(num);
		order.setOrderNumber(outTradeNo);
		order.setPaymentType(0);
		order.setPrice(answer);
		order.setType(type);
		order.setUid(uid);

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "棉花代理平台的商品收款";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = String.valueOf(answer);

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品"+num+"件共"+answer+"元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "会员充值", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "会员充值", 500, 2);
        goodsDetailList.add(goods2);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
            .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
            .setTimeoutExpress(timeoutExpress)
            .setNotifyUrl("http://116.255.190.125:8080/works/pcpay/notify_url")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
            .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
                String filename = "/qr-"+response.getOutTradeNo()+".png";
                String savedDir = request.getSession().getServletContext().getRealPath("qrcode");
                String filePath = String.format(savedDir+filename);
                log.info("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

                vordersService.insert(order);
				return ServerResponse.createBySuccess("处理成功","qrcode"+filename);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorCodeMessage(2,"处理失败");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorCodeMessage(2,"处理失败");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorCodeMessage(2,"处理失败");
        }
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
