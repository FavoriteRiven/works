package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.common.ServerResponse;
import com.project.dao.VCityMapper;
import com.project.pojo.VCity;

@Service("iVCityService")
public class VCityService extends BaseService<VCity>{

	@Autowired
	private VCityMapper vcm;
	
	public ServerResponse<VCity> selectByPrimaryKey(int id) {
		// TODO Auto-generated method stub
		return ServerResponse.createBySuccess("0", vcm.selectByPrimaryKey(id));
	}

	public ServerResponse<List<VCity>> selectByProvinceId(int id) {
		// TODO Auto-generated method stub
		return ServerResponse.createBySuccess("0", vcm.selectByProvinceId(id));
	}

}
