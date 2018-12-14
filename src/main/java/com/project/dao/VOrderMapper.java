package com.project.dao;

import com.project.pojo.VOrder;

public interface VOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VOrder record);

    int insertSelective(VOrder record);

    VOrder selectByPrimaryKey(String orderNum);

    int updateByPrimaryKeySelective(VOrder record);

    int updateByPrimaryKey(VOrder record);

	void updateByOrderNum(String out_trade_no);
}