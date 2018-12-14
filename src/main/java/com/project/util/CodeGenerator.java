package com.project.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

//各种随机数
public class CodeGenerator {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	

	public static String getOrderCode() {
		String code = "";
		for(int i=0;i<2;i++){
			code+=getUniqueString();
		}
		return code;
	}
	
	public static String getLoginToken() {
		return MD5Util.MD5EncodeUtf8(new Date().getTime() + getUniqueString()+getUniqueString());
	}
	
	public static String getPayCode() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		return key;
	}
	//根据指定长度生成字母和数字的随机数
    //0~9的ASCII为48~57
    //A~Z的ASCII为65~90
    //a~z的ASCII为97~122
	public static String getUserName() {

		int length=10;
		StringBuilder sb=new StringBuilder();
        Random rand=new Random();//随机用以下三个随机生成器
        Random randdata=new Random();
        int data=0;
        for(int i=0;i<length;i++)
        {
            int index=rand.nextInt(3);
            //目的是随机选择生成数字，大小写字母
            switch(index)
            {
            case 0:
                 data=randdata.nextInt(10);//仅仅会生成0~9
                 sb.append(data);
                break;
            case 1:
                data=randdata.nextInt(26)+65;//保证只会产生65~90之间的整数
                sb.append((char)data);
                break;
            case 2:
                data=randdata.nextInt(26)+97;//保证只会产生97~122之间的整数
                sb.append((char)data);
                break;
            }
        }
        String result=sb.toString();
        System.out.println(result);
		return result;
	}
	//根据指定长度生成字母和数字的随机数
    //0~9的ASCII为48~57
    //A~Z的ASCII为65~90
    //a~z的ASCII为97~122
	public static String getQRCode() {

		int length=40;
		StringBuilder sb=new StringBuilder();
        Random rand=new Random();//随机用以下三个随机生成器
        Random randdata=new Random();
        String base="#$";
        int data=0;
        for(int i=0;i<length;i++)
        {
            int index=rand.nextInt(3);
            //目的是随机选择生成数字，大小写字母
            switch(index)
            {
            case 0:
                 data=randdata.nextInt(10);//仅仅会生成0~9
                 sb.append(data);
                break;
            case 1:
                data=randdata.nextInt(26)+65;//保证只会产生65~90之间的整数
                sb.append((char)data);
                break;
            case 2:
                data=randdata.nextInt(base.length())+97;//保证只会产生97~122之间的整数
                sb.append(base.charAt(data-97));
                break;
            }
        }
        String result=sb.toString();
        System.out.println(result);
		return result;
	}
	public static String getUniqueString() {
		char[] array = {'0','1','2','3','4','5','6','7','8','9'};
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
		    int index = rand.nextInt(i);
		    char tmp = array[index];
		    array[index] = array[i - 1];
		    array[i - 1] = tmp;
		}
		String result = "";
		for(int i = 0; i < 6; i++)
		    result += array[i];
		return String.valueOf(result);
	}
	public static String getmealNumberString() {
		char[] array = {'0','1','2','3','4','5','6','7','8','9'};
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
		    int index = rand.nextInt(i);
		    char tmp = array[index];
		    array[index] = array[i - 1];
		    array[i - 1] = tmp;
		}
		String result = "";
		for(int i = 0; i < 9; i++)
		    result += array[i];
		for(int i = 0; i < 3; i++)
		    result += array[i];
		return String.valueOf(result);
	}

	 /** 
     * 生成随机文件名：当前年月日时分秒+五位随机数 
     *  
     * @return 
     */  
    public static String getRandomFileName() {  
  
        SimpleDateFormat simpleDateFormat;  
  
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
  
        Date date = new Date();  
  
        String str = simpleDateFormat.format(date);  
  
        Random random = new Random();  
  
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数  
  
        return  str+rannum;// 当前时间  
    }  
    /** 
     * 生成随机文件名：当前年月日 
     *  
     * @return 
     */  
    public static String getRandomFileName2() {  
  
        SimpleDateFormat simpleDateFormat;  
  
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");  
  
        Date date = new Date();  
  
        String str = simpleDateFormat.format(date);  
  
        return  str;// 当前时间  
    }  
    /** 
     * 生成：当前年月日+四位随机数 
     *  
     * @return 
     */  
    public static String getMealNumber() {  
  
        SimpleDateFormat simpleDateFormat;  
  
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");  
  
        Date date = new Date();  
  
        String str = simpleDateFormat.format(date);  
  
        Random random = new Random();  
  
        int rannum = (int) (random.nextDouble() * (9999 - 1000 + 1)) + 1000;// 获取5位随机数  
  
        return rannum + str;// 当前时间  
    }  
    /** 
     * 生成随机订单号：当前年月日时分秒+随机数 
     *  
     * @return 
     */  
    public static String getOrderCodeTime() {  
  
        SimpleDateFormat simpleDateFormat;  
  
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
  
        Date date = new Date();  
  
        String str = simpleDateFormat.format(date);  
  
        String rannum = getOrderCode();
  
        return str + rannum;// 当前时间  
    } 
    
    /**
     * 生成六位验证码
     * @return
     */
    public static String getSixYZM(){
    	Random random = new Random();
    	int rannum = (int)(random.nextDouble() * (999999 - 100000 + 1)) +100000;
    	return "" + rannum;
    }
}
