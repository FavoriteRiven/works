package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dao.VOrderMapper;
import com.project.pojo.VOrder;

@Service("iVOrderService")
public class VOrderService extends BaseService<VOrder>{

	@Autowired
	private VOrderMapper vorderMapper;
	
	public VOrder selectByPrimaryKey(String orderId) {
		// TODO Auto-generated method stub
		return vorderMapper.selectByPrimaryKey(orderId);
	}

	public void insert(VOrder order) {
		// TODO Auto-generated method stub
		vorderMapper.insert(order);
	}

	public void updateByOrderNum(String out_trade_no) {
		// TODO Auto-generated method stub
		vorderMapper.updateByOrderNum(out_trade_no);
	}

}
