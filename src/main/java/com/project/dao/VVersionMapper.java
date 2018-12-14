package com.project.dao;

import com.project.common.ServerResponse;
import com.project.pojo.VVersion;

public interface VVersionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VVersion record);

    int insertSelective(VVersion record);

    VVersion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VVersion record);

    int updateByPrimaryKey(VVersion record);

	VVersion selectByApptype(Integer apptype);
}