package com.project.dao;

import java.util.List;

import com.project.pojo.VCity;

public interface VCityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VCity record);

    int insertSelective(VCity record);

    VCity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VCity record);

    int updateByPrimaryKey(VCity record);

	List<VCity> selectByProvinceId(int id);

	String selectCity(Integer cityId);
}