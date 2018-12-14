package com.project.dao;

import java.util.List;
import java.util.Map;

import com.project.pojo.VLine;

public interface VLineMapper extends BaseMapper<VLine>{
    int deleteByPrimaryKey(Integer id);

    int insert(VLine record);

    int insertSelective(VLine record);

    VLine selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VLine record);

    int updateByPrimaryKey(VLine record);

	List<VLine> selectAlldynamic();

	List<VLine> selectAllstatic();

	void updateByLineId(Integer currentLineId);

	void updateByLineIdAdd(Integer line_id);

	List<VLine> selectAllLine(Map<String,Object> param);
	
	Integer selectCount();

	List<VLine> selectBycid(VLine vl2);
}