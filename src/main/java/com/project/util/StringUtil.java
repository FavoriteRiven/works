package com.project.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {
	
	//非空
	public static boolean isEmpty(String str) {
		return null == str || str.trim().length() == 0;
	}
	
	public static String UTF8(String str){
		try {
			str=new String(str.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

}
