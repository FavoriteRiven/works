package com.project.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.project.pojo.VCode;

public interface VCodeMapper extends BaseMapper<VCode>{
    int deleteByPrimaryKey(Integer id);

    int insert(VCode record);

    int insertSelective(VCode record);

    VCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VCode record);

    int updateByPrimaryKey(VCode record);
    
    int getCode(@Param("phone")String phone,@Param("code")String code);

	int checkByPhone(String phone);

	void updateByPhone(VCode code);

	Date getValidity(String phone);
}