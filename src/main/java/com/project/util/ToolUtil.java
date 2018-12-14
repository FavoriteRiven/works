package com.project.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

//工具类 汉字转拼音，返回中文的首字母，将字符串转移为ASCII码，计算百度地图两个位置间的距离
public class ToolUtil {
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String getSmallPic(String pic) {
		try {
		    if(pic.indexOf("_small")!=-1) return pic;
		    String picName = pic.substring(0,pic.lastIndexOf("."));
		    picName = picName+"_small";
		    String ext = pic.substring(pic.lastIndexOf("."));
		    return picName + ext;
		} catch (Exception e) {
		   ;
		}
		return pic;
	}
	
	//汉字转拼音
	public static String getPingYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断是否为汉字字符
				if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4 += t2[0];
				} else
					t4 += java.lang.Character.toString(t1[i]);
			}
			// System.out.println(t4);
			return t4;
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}

	// 返回中文的首字母
	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}

	// 将字符串转移为ASCII码
	public static String getCnASCII(String cnStr) {
		StringBuffer strBuf = new StringBuffer();
		byte[] bGBK = cnStr.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			// System.out.println(Integer.toHexString(bGBK[i]&0xff));
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
		}
		return strBuf.toString();
	}

	public static String getRemoteAddrIp(HttpServletRequest request) {
		try {
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("PRoxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) ) {
				ip = request.getRemoteAddr();
			}
			return ip;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	
	public static String getIpAddr(HttpServletRequest request) {  
	    String ipFromNginx = getHeader(request, "X-Real-IP");  
	    return StringUtil.isEmpty(ipFromNginx) ? getRemoteAddrIp(request) : ipFromNginx;  
	}  
	  
	  
	private static String getHeader(HttpServletRequest request, String headName) {  
	    String value = request.getHeader(headName);
	    return !StringUtil.isEmpty(value) && !"unknown".equalsIgnoreCase(value) ? value : "";  
	}  
	
	// 获取用户所在城市
	public static String getClientCityFromIP(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		try {
			String strURL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=112.84.88.216";
			//String strURL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + getIpAddr(request);
			System.out.println(strURL);
			URL url = new URL(strURL);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) connection;
			System.setProperty("sun.net.client.defaultConnectTimeout", "150000");
			System.setProperty("sun.net.client.defaultReadTimeout", "150000");
			httpConn.setRequestMethod("GET");// 设置请求为POST方法
			connection.setDoOutput(true);// 可以输出
			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "GBK");
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				temp = temp.trim();
				if (temp != null && temp.length() > 0) {
					sb.append(temp);
				}
			}
			br.close();
			isr.close();

			String jsonstr = sb.toString().substring(sb.toString().indexOf('{'), sb.toString().length() - 1);
			JSONObject obj = JSONObject.fromObject(jsonstr);
			return obj.getString("city");

		} catch (Exception e) {
		    e.printStackTrace();
			System.err.println("GetCity.Error");
		}
		return "";
	}

	// 计算百度地图两个位置间的距离
	static double DEF_PI = 3.14159265359; // PI
	static double DEF_2PI = 6.28318530712; // 2*PI
	static double DEF_PI180 = 0.01745329252; // PI/180.0
	static double DEF_R = 6370693.5; // radius of earth

	public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2) {
		double ew1, ns1, ew2, ns2;
		double dx, dy, dew;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 经度差
		dew = ew1 - ew2;
		// 若跨东经和西经180 度，进行调整
		if (dew > DEF_PI)
			dew = DEF_2PI - dew;
		else if (dew < -DEF_PI)
			dew = DEF_2PI + dew;
		dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
		// 勾股定理求斜边长
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	public static double GetLongDistance(double lon1, double lat1, double lon2, double lat2) {
		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0)
			distance = 1.0;
		else if (distance < -1.0)
			distance = -1.0;
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance;
	}

}
