package com.project.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.project.common.ServerResponse;
import com.project.pojo.VConsoleUser;
import com.project.pojo.VUser;

public interface VUserMapper extends BaseMapper<VUser>{
    int deleteByPrimaryKey(Integer id);

    int insert(VUser record);

    int insertSelective(VUser record);

    VUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VUser record);

    int updateByPrimaryKey(VUser record);
    
    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);

    int updatePasswordByPhone(@Param("phone")String phone,@Param("passwordNew")String passwordNew);
    
    VUser selectLogin(@Param("username") String username, @Param("password")String password);
    
    VUser selectPhoneLogin(@Param("phone") String phone);
    
    int checkUsername(String username);
    
    int checkUserPhone(String phone);

    int checkPassword(@Param(value="password")String password,@Param("userId")Integer userId);

	int updatePayPwdByPhone(@Param("phone")String phone,@Param("payPwd")String payPwd);

	VUser selectNetwork();

	int checkUserById(int id);

	void updateLineIp(@Param("line_id")Integer line_id, @Param("uid")Integer uid);

	void updateByUid(@Param("uid")Integer uid, @Param("date")Date date, @Param("expireTime")String expireTime,@Param("maxlogin") Integer maxlogin,@Param("ismember") Integer ismember);

	void updateByUid2(@Param("uid")Integer uid, @Param("expireTime")String expireTime,@Param("maxlogin") Integer maxlogin,@Param("ismember") Integer ismember);
	
	VConsoleUser consoleLogin(@Param("username")String username, @Param("password")String password);

	List<VUser> selectAllUser(Map<String, Object> param);

	int updateConsoleInfo(@Param("username")String username, @Param("password")String password,
			@Param("id")Long id);

	Integer selectCount();

	VUser checkByOpenid(String openid);

}