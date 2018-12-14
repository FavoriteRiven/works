package com.project.controller.console;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.common.ServerResponse;
import com.project.pojo.VMember;
import com.project.service.VMemberService;

/**
 * @author Administrator
 * 后台会员管理
 *
 */
@CrossOrigin
@Controller
@RequestMapping("/consolemember/")
public class MemeberConsoleController {

	@Autowired
	private VMemberService iVMemberService;
	
	/**
	 * 获取后台会员列表
	 * @return
	 */
	@RequestMapping(value="info",method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<List<VMember>> getMemeber(){
		return iVMemberService.selectAll();
	}
	
	/**
	 * 修改后台会员信息
	 * @param vm
	 * @return
	 */
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> update(VMember vm){
		return iVMemberService.updateMemeber(vm);
	}
	
}
