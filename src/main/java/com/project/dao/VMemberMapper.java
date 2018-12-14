package com.project.dao;

import java.util.List;

import com.project.pojo.VMember;

public interface VMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VMember record);

    int insertSelective(VMember record);

    VMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VMember record);

    int updateByPrimaryKey(VMember record);

	List<VMember> selectAll();

	VMember selectByType(Integer type);
}