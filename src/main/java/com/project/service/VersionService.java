package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.common.ServerResponse;
import com.project.dao.VVersionMapper;
import com.project.pojo.VVersion;

@Service("iVversionService")
public class VersionService extends BaseService<VVersion>{

	@Autowired
	private VVersionMapper vvm;

	public ServerResponse<VVersion> selectByKey(Integer apptype) {
		// TODO Auto-generated method stub
		return ServerResponse.createBySuccess(vvm.selectByApptype(apptype));
	}
}
