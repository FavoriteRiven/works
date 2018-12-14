package com.project.controller.portal;

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
@CrossOrigin
@Controller
@RequestMapping("/member/")
public class VMemberController {

	@Autowired
	private VMemberService iVMemberService;
	
	@RequestMapping(value = "info",method = RequestMethod.GET)
    @ResponseBody
	public ServerResponse<List<VMember>> getMember(){
	        ServerResponse<List<VMember>> response = iVMemberService.selectAll();
	        return response;
	}
}
