package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.common.ServerResponse;
import com.project.dao.VProvinceMapper;
import com.project.pojo.VProvince;


@Service("iVProvinceService")
public class VProvinceService extends BaseService<VProvince>{
	
	@Autowired
	private VProvinceMapper vm;

	public ServerResponse<List<VProvince>> selectAll() {
		// TODO Auto-generated method stub
		
		return ServerResponse.createBySuccess("0", vm.selectAll());
	}

}
