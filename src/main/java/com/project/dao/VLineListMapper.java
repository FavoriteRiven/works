package com.project.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized.Parameters;

import com.project.pojo.VLineList;

public interface VLineListMapper extends BaseMapper<VLineList>{

	VLineList selectByVlineId(@Param("lid")Integer lid,@Param("beforeid")Integer beforeid);

	void updateByLineListId(Integer id);

	int insertSelective(VLineList vlist);

	List<VLineList> selectAllLineList(Map<String, Object> param);

	Integer selectCount();

	int updateLineListState(Integer lid);

	int updateByPrimaryKeySelective1(VLineList list);
	
}
