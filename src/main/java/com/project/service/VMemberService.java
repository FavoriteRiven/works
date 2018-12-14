package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.common.ServerResponse;
import com.project.dao.VMemberMapper;
import com.project.pojo.VMember;

@Service("iVMemberService")
public class VMemberService {
	
	@Autowired
	private VMemberMapper vmm;
	
	public ServerResponse<List<VMember>> selectAll() {
		// TODO Auto-generated method stub
		List<VMember> selectAll = vmm.selectAll();
		for (VMember vMember : selectAll) {
			if(vMember.getCompany().equals("1")){
				vMember.setCompanyRem("天");
			}else if(vMember.getCompany().equals("2")){
				vMember.setCompanyRem("月");
			}else if(vMember.getCompany().equals("3")){
				vMember.setCompanyRem("季");
			}else if(vMember.getCompany().equals("4")){
				vMember.setCompanyRem("半年");
			}else if(vMember.getCompany().equals("5")){
				vMember.setCompanyRem("年");
			}else if(vMember.getCompany().equals("6")){
				vMember.setCompanyRem("永久");
			}
		}
		return ServerResponse.createBySuccess(selectAll);
	}

	public ServerResponse<String> updateMemeber(VMember vm) {
		// TODO Auto-generated method stub
		int i = vmm.updateByPrimaryKey(vm);
		if (i == 1) {
			return ServerResponse.createBySuccessMessage("成功");
		}
		return ServerResponse.createBySuccessMessage("失败");
	}

	public VMember selectByKey(int i) {
		// TODO Auto-generated method stub
		VMember key = vmm.selectByPrimaryKey(i);
		return key;
	}

	public VMember selectByType(Integer type) {
		// TODO Auto-generated method stub
		return vmm.selectByType(type);
	}

}
