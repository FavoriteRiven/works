package com.project.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.common.Const;
import com.project.common.ServerResponse;
import com.project.common.TokenCache;
import com.project.dao.VCodeMapper;
import com.project.dao.VUserMapper;
import com.project.pojo.VCode;
import com.project.pojo.VConsoleUser;
import com.project.pojo.VLine;
import com.project.pojo.VUser;
import com.project.util.MD5Util;

/**
 * Created by Super
 */
@Service("iVUserService")
public class VUserService extends BaseService<VUser> {
	
	private static  final Logger logger = LoggerFactory.getLogger(VUserService.class);

    @Autowired
    private VUserMapper VUserMapper;
    
    @Autowired
    private VCodeMapper codeMapper;

    /**app登录
     * @param username
     * @param password
     * @return
     */
    public ServerResponse<VUser> login(String username, String password,String openid) {
    	
    	
    	if(openid!=null){
    		VUser u = VUserMapper.checkByOpenid(openid);
    		if(u!=null){
    			return ServerResponse.createBySuccess("登录成功",u);
    		}else{
    			return ServerResponse.createByErrorMessage("请绑定手机号");
    		}
    	}
    	
        int resultCount = VUserMapper.checkUsername(username);
        if(resultCount == 0 ){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        VUser VUser  = VUserMapper.selectLogin(username,md5Password);

        if(VUser == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }else if(VUser.getCurrentConnection()>=VUser.getMaxlogin()){
        	return ServerResponse.createByErrorCodeMessage(1, "登录人数已满请升级更高套餐");
        }
        
        //修改登录数
        VUser.setCurrentConnection(VUser.getCurrentConnection()+1);
        VUser.setUserType((byte) 1);
        VUserMapper.updateByPrimaryKey(VUser);

        VUser.setPassWord(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",VUser);
    }
    
    
    
    /**
     * 后台登录
     * @param username
     * @param password
     * @return
     */
    public ServerResponse<VConsoleUser> consoleLogin(String username, String password) {
        VConsoleUser consoleUser = VUserMapper.consoleLogin(username,password);
        if(consoleUser == null){
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }
        return ServerResponse.createBySuccess("登录成功", consoleUser);
    }
    
    
    public ServerResponse<VUser> phoneLogin(String phone, String pwd) {
    	//从数据库查找验证码
/*    	if (!verify.equals("null")) {
    		int flag = codeMapper.getCode(verify);
        	if (flag==0) {
        		return ServerResponse.createByErrorCodeMessage(3,"验证码不正确");
    		}
		}*/
    	VUser VUser  = VUserMapper.selectPhoneLogin(phone);
        if(VUser == null ){
            return ServerResponse.createByErrorCodeMessage(1,"手机未注册");
        }
        
        //密码验证
        if(!pwd.equals("null") && !VUser.getPassWord().equals(MD5Util.MD5EncodeUtf8(pwd)) ){
            return ServerResponse.createByErrorCodeMessage(2,"密码错误");
        }

        VUser.setPassWord(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",VUser);
    }



    public ServerResponse<String> register(String phone,String password,String verify,String openid){
        int count = VUserMapper.checkUserPhone(phone);
        if(count != 0){
            return ServerResponse.createByErrorCodeMessage(1,"手机号已存在");
        }
        //验证码验证
    	if (verify != null) {
    		int flag = codeMapper.getCode(phone,verify);
        	if (flag==0) {
        		return ServerResponse.createByErrorCodeMessage(3,"验证码不正确");
    		}
		}
    	if (verify != null) {
    		Date date = codeMapper.getValidity(phone);
    		Date now = new Date();
        	if (now.getTime()>date.getTime()) {
        		return ServerResponse.createByErrorCodeMessage(3,"验证码已失效");
    		}
		}
        VUser VUser = new VUser(); 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		Calendar ca=Calendar.getInstance();
		ca.setTime(now);
		ca.add(Calendar.HOUR_OF_DAY, 1);
		String format = sdf.format(ca.getTime());
		Date ExpireTime = null;
		try {
			ExpireTime = sdf.parse(format);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        VUser.setPhone(phone);
        //如果不是第三方登录 用户名默认为手机号
        VUser.setUserName(phone);
        VUser.setCreateTime(now);
        VUser.setRechargeTime(now);
        VUser.setIsfilter((byte)0);
        VUser.setIslogin((byte)0);
        VUser.setIsmember((byte)0);
        VUser.setUserType((byte) 0);
        VUser.setCurrentConnection(0);
        VUser.setExpireTime(ExpireTime);
        VUser.setMaxlogin(2);
        if(openid!=null){
        	VUser.setOpenid(openid);
        }
        //MD5加密
        VUser.setPassWord(MD5Util.MD5EncodeUtf8(password));
        VUserMapper.insert(VUser);
        
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str,String type){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = VUserMapper.checkUsername(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse<String> forgetResetPassword(String VUsername,String passwordNew,String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        ServerResponse validResponse = this.checkValid(VUsername,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+VUsername);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            String md5Password  = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = VUserMapper.updatePasswordByPhone(VUsername,md5Password);

            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }


    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,VUser VUser){
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        int resultCount = VUserMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),VUser.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        VUser.setPassWord(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = VUserMapper.updateByPrimaryKeySelective(VUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }


    public ServerResponse<String> updatePhone(VUser VUser){
        VUser updateVUser = new VUser();
        updateVUser.setId(VUser.getId());
        updateVUser.setPhone(VUser.getPhone());

        int updateCount = VUserMapper.updateByPrimaryKeySelective(updateVUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新成功",updateVUser.getPhone());
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }



    public ServerResponse<VUser> getInformation(Integer VUserId){
        VUser VUser = (VUser)VUserMapper.selectByPrimaryKey(VUserId);
        VUser url = VUserMapper.selectNetwork();
        if(VUser == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        VUser.setNetwork(url.getNetwork());
        VUser.setPassWord(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(VUser);

    }

    //backend

    /**
     * 校验是否是管理员
     * @param VUser
     * @return
     */
    public ServerResponse checkAdminRole(VUser VUser){
        /*if(VUser != null && VUser.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }*/
        return ServerResponse.createByError();
    }

//	@Override
//	public int saveCode(String verify) {
//		//有效期
////		long currentTime = System.currentTimeMillis() ;
////		currentTime += 5*60*1000;
////		Date validity = new Date(currentTime);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date now = new Date();
//		Date afterDate = new Date(now.getTime() + 300000);
//		Code code = new Code();
//		code.setCode(verify);
//		code.setValidity(afterDate);
//		return codeMapper.saveCode(code);
//	}

	public ServerResponse<String> updatePwd(String phone, String passwordNew, String verify) {
		int count = VUserMapper.checkUserPhone(phone);
		if(count != 1){
		    return ServerResponse.createByErrorCodeMessage(1,"手机号不存在");
		}
		//验证码验证
		if (verify != null) {
			int flag = codeMapper.getCode(phone,verify);
			if (flag==0) {
				return ServerResponse.createByErrorCodeMessage(2,"验证码不正确");
			}
		}
		
		if (verify != null) {
    		Date date = codeMapper.getValidity(phone);
    		Date now = new Date();
        	if (now.getTime()>date.getTime()) {
        		return ServerResponse.createByErrorCodeMessage(3,"验证码已失效");
    		}
		}
		
		int resultCount = VUserMapper.updatePasswordByPhone(phone, MD5Util.MD5EncodeUtf8(passwordNew));
		if(resultCount == 0){ 
		    return ServerResponse.createByErrorCodeMessage(3,"修改失败");
		}
		return ServerResponse.createByErrorCodeMessage(0,"修改成功");
	}

	public int checkUserById(int id) {
		return VUserMapper.checkUserById(id);
	}

	public int saveCode(String phone,String verify) {
		//有效期
//		long currentTime = System.currentTimeMillis() ;
//		currentTime += 5*60*1000;
//		Date validity = new Date(currentTime);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		Date afterDate = new Date(now.getTime() + 300000);
		VCode code = new VCode();
		code.setVerifyCode(verify);
		code.setValidity(afterDate);
		code.setPhone(phone); 
		int isture = codeMapper.checkByPhone(phone);
		if(isture!=0){
			codeMapper.updateByPhone(code);
			return 1;
		}
		codeMapper.insert(code);
		return 1;
	}

	public ServerResponse<String> updatePayPwd(String phone, String passwordNew, String verify) {
		int count = VUserMapper.checkUserPhone(phone);
		if(count != 1){
		    return ServerResponse.createByErrorCodeMessage(1,"手机号不存在");
		}
		//验证码验证
		if (verify != null) {
			int flag = codeMapper.getCode(phone,verify);
			if (flag==0) {
				return ServerResponse.createByErrorCodeMessage(2,"验证码不正确");
			}
		}
		int resultCount = VUserMapper.updatePayPwdByPhone(phone, MD5Util.MD5EncodeUtf8(passwordNew));
		if(resultCount == 0){ 
		    return ServerResponse.createByErrorCodeMessage(3,"修改失败");
		}
		return ServerResponse.createByErrorCodeMessage(0,"修改成功");
	}

	public void updateLineIp(Integer line_id, Integer uid) {
		// TODO Auto-generated method stub
		VUserMapper.updateLineIp(line_id,uid);
	}

	public void updateByUid(Integer uid, Date date, String expireTime,Integer maxlogin,Integer ismember) {
		// TODO Auto-generated method stub
		VUserMapper.updateByUid(uid,date,expireTime,maxlogin,ismember);
	}


	public Map<String,Object> selectAllUser(int pageNum,int pageSize,String phone) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("pageNum", (pageNum-1)*pageSize);
		param.put("pageSize", pageSize);
		if(phone!=null){
			param.put("phone", "%"+phone+"%");
		}
		List<VUser> list=VUserMapper.selectAllUser(param);
		Integer count=VUserMapper.selectCount();
		Map<String,Object> resultMap=new HashMap<String,Object>();
		resultMap.put("code", 0);
		resultMap.put("msg", "查询成功");
		resultMap.put("data", list);
		resultMap.put("count", count);
		return resultMap;		
	}


	public int updateByKey(VUser user) {
		// TODO Auto-generated method stub
		return VUserMapper.updateByPrimaryKeySelective(user);
	}


	public ServerResponse<String> updateConsoleInfo(String username,
			String password, Long id) {
		// TODO Auto-generated method stub
		int i = VUserMapper.updateConsoleInfo(username,password,id);
		if (i!=0) {
			return ServerResponse.createBySuccessMessage("修改成功");
		}
		return ServerResponse.createByErrorMessage("修改失败");
	}



	public ServerResponse<String> layout(Integer id) {
		// TODO Auto-generated method stub
		VUser user = VUserMapper.selectByPrimaryKey(id);
		
		if(user.getCurrentConnection()==0){
			return ServerResponse.createByErrorCodeMessage(3, "未登录");
		}
		Integer j = user.getCurrentConnection()-1;
		user.setCurrentConnection(j);
		if(j==0){
			user.setUserType((byte) 0);
		}
		int i = VUserMapper.updateByPrimaryKey(user);
		if (i!=0) {
			return ServerResponse.createBySuccessMessage("成功");
		}
		return ServerResponse.createByErrorMessage("失败");
	}



	public void updateByUid2(Integer uid, String expireTime, Integer maxlogin,Integer ismember) {
		// TODO Auto-generated method stub
		VUserMapper.updateByUid2(uid,expireTime,maxlogin,ismember);
	}



	public com.project.pojo.VUser getInformation_pc(int uId) {
		// TODO Auto-generated method stub
		Integer id = Integer.valueOf(uId);
		return VUserMapper.selectByPrimaryKey(id);
	}



	public ServerResponse<String> registWx(String phone, String password,
			String openid) {
		VUser VUser  = VUserMapper.selectPhoneLogin(phone);
        if(VUser == null ){
            return ServerResponse.createByErrorCodeMessage(1,"手机未注册");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        VUser VUser1  = VUserMapper.selectLogin(phone,md5Password);

        if(VUser1 == null|| VUser1.getPhone()==null||VUser1.getPhone()==""){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        System.out.println(VUser1);
        VUser use = new VUser();
        use.setId(VUser1.getId());
        use.setOpenid(openid);
        int i = VUserMapper.updateByPrimaryKeySelective(use);
        if (i!=0) {
        	return ServerResponse.createBySuccessMessage("绑定成功");
		}
		return ServerResponse.createByErrorMessage("绑定失败");
	}

}

