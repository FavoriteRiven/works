package com.project.controller.console;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.common.ServerResponse;
import com.project.dao.VUserMapper;
import com.project.pojo.VConsoleUser;
import com.project.pojo.VUser;
import com.project.service.VUserService;
import com.project.util.MD5Util;

@CrossOrigin
@Controller
@RequestMapping("/admin/")
public class AdminConsoleController {

	@Autowired
	private VUserService iVUserService;
	
	@Autowired 
	private VUserMapper iusermapper;
	
	/**
	 * 管理员后台登录
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "login",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<VConsoleUser> login(String username,String password){
		String pass = MD5Util.MD5EncodeUtf8(password);
		return iVUserService.consoleLogin(username, pass);		
	}
	
	/**
	 * 修改用户名或密码
	 * @param username
	 * @param password
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "upduserinfo",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> updUserInfo(String username,String password,Long id){
		String pass = MD5Util.MD5EncodeUtf8(password);
		return iVUserService.updateConsoleInfo(username,pass,id);		
	}
	
	
	/**
	 * 查询所有用户信息
	 * @return
	 */
	@RequestMapping(value = "userinfo",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> info(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, 
			@RequestParam(value = "pageSize",defaultValue = "10") int pageSize,String phone){
		if(phone==null||phone==""){
			phone = null;
		}
		Map<String,Object> resultMap = iVUserService.selectAllUser(pageNum, pageSize,phone);		
		return resultMap;
	}
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "update",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> update(VUser user,Long exetime){
		if (user.getPhone()!=null) {
			int count = iusermapper.checkUserPhone(user.getPhone());
	        if(count != 0){
	            return ServerResponse.createByErrorCodeMessage(1,"手机号已存在");
	        }
		}
		Date date = new Date();
		user.setUpdateTime(date);
		if(exetime!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
			String sd = sdf.format(new Date(exetime));
			Date parse = null;
			try {
				parse = sdf.parse(sd);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			user.setExpireTime(parse);
		}
		int i = iVUserService.updateByKey(user);
		if (i!=0) {
			return ServerResponse.createBySuccessMessage("修改成功");
		}
		return ServerResponse.createByErrorMessage("修改失败");		
	}
	
	/**
	 * 删除用户信息
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "delete/{uid}",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> delete(@PathVariable("uid")Integer uid){
		int i = iVUserService.delById(uid);
		if (i!=0) {
			return ServerResponse.createBySuccessMessage("删除成功");
		}
		return ServerResponse.createByErrorMessage("删除失败");		
	}
	
	/**
	 * 定时访问接口
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "timer/{uid}",method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> timer(@PathVariable("uid")Integer uid){
		VUser user = (VUser) iVUserService.selectById(uid);
		VUser newuser = new VUser();
		newuser.setId(uid);
		newuser.setIslogin((byte) 0);
		if(user!=null){
			long time = user.getExpireTime().getTime();
			Date date = new Date();
			long time2 = date.getTime();
			if(user.getIslogin()==0){
				return ServerResponse.createByErrorMessage("断开");
			}else if(time-time2<0){
				iVUserService.updateByKey(newuser);
				return ServerResponse.createByErrorMessage("断开");
			}else{
				return ServerResponse.createBySuccessMessage("链接");
			}
		}
		return ServerResponse.createByErrorCodeMessage(3, "参数错误");
	}
	
}
