package com.project.controller.portal;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.google.common.collect.Maps;
import com.project.common.ServerResponse;
import com.project.pojo.VUser;
import com.project.service.FileService;
import com.project.service.VUserService;
import com.project.util.AliyunAPI;
import com.project.util.PropertiesUtil;
@CrossOrigin
@Controller
@RequestMapping("/user/")
public class VUserController {

    @Autowired
    private VUserService iVUserService;
    
    @Autowired
    private FileService iFileService;
    
    private static String qq = PropertiesUtil.getProperty("qq");

    /**
     * 用户手机登录
     * @param Username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<VUser> phone_login(String phone,@RequestParam(value = "password",defaultValue = "null") String password ,String openid){
    	
        ServerResponse<VUser> response = iVUserService.login(phone,password,openid);
        
        return response;
    }
    
    /**
     * 获取qq
     * @return
     */
    @RequestMapping(value = "getqq",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> getqq(){
    	return ServerResponse.createBySuccess("成功", qq);
    }
    
    /**
     * 绑定微信
     * @param phone
     * @param password
     * @param openid
     * @return
     */
    @RequestMapping(value = "registWx",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> registWx(String phone,@RequestParam(value = "password",defaultValue = "null") String password ,String openid){
    	
        ServerResponse<String> response = iVUserService.registWx(phone,password,openid);
        
        return response;
    }
    
    /**退出登录
     * @param id
     * @return
     */
    @RequestMapping(value = "layout",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> layout(Integer uid){
    	
    	return iVUserService.layout(uid);
    }
    
    
    //登录前忘记密码
    @RequestMapping(value = "forget/password",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetPwd(String phone,String passwordNew,String verify){
        return iVUserService.updatePwd(phone,passwordNew,verify);
    }
    
    //注册
    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(String phone,String password,String verify,String openid){
        return iVUserService.register(phone,password,verify,openid);
    }
    
    //获取用户信息
    @RequestMapping(value = "info/{uId}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<VUser> getVUserInfo(@PathVariable("uId") int uId){
        int VUser =  iVUserService.checkUserById(uId);
        if(VUser != 1){
        	return ServerResponse.createByErrorCodeMessage(3, "用户不存在或未登录");
        }
        return iVUserService.getInformation(uId);
    }
    
    //pc获取用户信息
    @RequestMapping(value = "infopc/{uId}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<VUser> getVUserInfo_pc(@PathVariable("uId") int uId){
        int VUser =  iVUserService.checkUserById(uId);
        if(VUser != 1){
        	return ServerResponse.createByErrorCodeMessage(3, "用户不存在或未登录");
        }
        VUser response = iVUserService.getInformation_pc(uId);

        
        if (null != response.getExpireTime()) {
        	Date time = response.getExpireTime();
        	String timestamp = String.valueOf(time.getTime());
            int length = timestamp.length();
            if (length > 3) {
            	String valueOf = timestamp.substring(0,length-3);
            	response.setPcExpireTime(valueOf);
                return ServerResponse.createBySuccess(response);
            } else {
                return ServerResponse.createByErrorMessage("参数错误");
            }
        }
        
        return ServerResponse.createByErrorMessage("参数错误");
    }
    
    
    //修改支付密码
    @RequestMapping(value = "pay_password",method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<String> updatePayPwd(String phone,String passwordNew,String verify){
    	return iVUserService.updatePayPwd(phone,passwordNew,verify);
    }
    
    //登录状态修改密码
    @RequestMapping(value = "password",method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String VUsername,String passwordNew,String forgetToken){
        return iVUserService.forgetResetPassword(VUsername,passwordNew,forgetToken);
    }


    //根据旧密码重置密码
    @RequestMapping(value = "reset_password",method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<String> resetPassword(int mId,String passwordOld,String passwordNew){
    	int i = iVUserService.checkUserById(mId);
    	if(i == 0){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        VUser VUser = new VUser();
        VUser.setId(mId);
        return iVUserService.resetPassword(passwordOld,passwordNew,VUser);
    }

    //更换手机号
    @RequestMapping(value = "phone",method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<String> updatePhone(int mId,String phone){
    	VUser VUser = new VUser();
    	VUser.setPhone(phone);
    	VUser.setId(mId);
        int currentVUser = iVUserService.checkUserById(mId);
        if(currentVUser == 0){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        ServerResponse<String> response = iVUserService.updatePhone(VUser);
       
        return response;
    }
    
    //上传头像
    @RequestMapping(value = "head_img",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(Integer mId,@RequestParam(value = "uploadFile",required = false) MultipartFile file,HttpServletRequest request){
    	
        String path = request.getSession().getServletContext().getRealPath("headImg");
        String targetFileName = iFileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+"/headImg/"+targetFileName;

        return ServerResponse.createBySuccess(url);
    }
    
    //多文件上传
    @RequestMapping(value = "file",method = RequestMethod.POST)
    @ResponseBody
    public Map uploadList(HttpServletRequest request,@RequestParam("files") MultipartFile[] files) { 
    	String path = request.getSession().getServletContext().getRealPath("headImg");
        //判断file数组不能为空并且长度大于0  
        if(files!=null&&files.length>0){
        	Map fileMap = Maps.newHashMap();
            //循环获取file数组中得文件  
            for(int i = 0;i<files.length;i++){  
                MultipartFile file = files[i];  
                //保存文件  
                String targetFileName = iFileService.upload(file,path);
                String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+"/headImg/"+targetFileName;
                fileMap.put("url",url);
            }  
            return fileMap;
        }
        return null;
    }  
    
    /**
     * 用户发送手机验证码
     * @param phone
     */
    @RequestMapping(value = "code",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse sendCode(String phone){
        String verify = String.valueOf((new Random().nextInt(899999) + 100000));
        try {
			SendSmsResponse response = AliyunAPI.sendIdentifyingCode(phone, verify);
			//存入数据库
			iVUserService.saveCode(phone,verify);
			
			return ServerResponse.createBySuccessMessage("发送成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ServerResponse.createByErrorMessage("短信发送异常！");
    }

}
