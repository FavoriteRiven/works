package com.project.dao;

import java.util.List;

import com.project.pojo.VProvince;

public interface VProvinceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VProvince record);

    int insertSelective(VProvince record);

    VProvince selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VProvince record);

    int updateByPrimaryKey(VProvince record);

	List<VProvince> selectAll();
}