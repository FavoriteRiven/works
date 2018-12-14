package com.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.common.ServerResponse;
import com.project.dao.VCityMapper;
import com.project.dao.VLineListMapper;
import com.project.dao.VLineMapper;
import com.project.pojo.VLine;
import com.project.pojo.VLineList;

@Service("iVLineService")
public class VLineService extends BaseService<VLine>{
	
	@Autowired
	private VLineMapper vlm;
	
	@Autowired 
	private VCityMapper vc;
	
	@Autowired
	private VLineListMapper vl;
	
	public Map<String,Object> selectAllLine(int pageNum,int pageSize){
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("pageNum", (pageNum-1)*pageSize);
		param.put("pageSize", pageSize);
		List<VLine> list=vlm.selectAllLine(param);
		for (VLine vLine : list) {
			String cName = vc.selectCity(vLine.getCityId());
			vLine.setCityName(cName);
		}
		Integer count=vlm.selectCount();
		Map<String,Object> resultMap=new HashMap<String,Object>();
		resultMap.put("code", 0);
		resultMap.put("msg", "查询成功");
		resultMap.put("data", list);
		resultMap.put("count", count);
		return resultMap;		
	};

	public ServerResponse<List<VLine>> selectAlldynamic() {
		// TODO Auto-generated method stub
		return ServerResponse.createBySuccess("0", vlm.selectAlldynamic());
	}

	public ServerResponse<List<VLine>> selectAllstatic() {
		// TODO Auto-generated method stub
		return ServerResponse.createBySuccess("0", vlm.selectAllstatic());
	}

	public void updateByLineId(Integer currentLineId) {
		// TODO Auto-generated method stub
		vlm.updateByLineId(currentLineId);
	}

	public void updateByLineIdAdd(Integer line_id) {
		// TODO Auto-generated method stub
		vlm.updateByLineIdAdd(line_id);
	}

	public ServerResponse<String> insert(VLine vl) {
		// TODO Auto-generated method stub
		int i = vlm.insert(vl);
		if (i == 0) {
			return ServerResponse.createByErrorMessage("失败");
		}
		return ServerResponse.createBySuccessMessage("成功");
	}

	public ServerResponse<String> updateLIine(VLine vl) {
		// TODO Auto-generated method stub
		int i = vlm.updateByPrimaryKeySelective(vl);
		if (i == 1) {
			return ServerResponse.createBySuccessMessage("成功");
		}
		return ServerResponse.createByErrorMessage("失败");
	}

	public VLineList selectLineList(Integer lid,Integer beforeid) {
		// TODO Auto-generated method stub
		return vl.selectByVlineId(lid,beforeid);
	}

	public void updateByLineListId(Integer id) {
		// TODO Auto-generated method stub
		vl.updateByLineListId(id);
	}

	public ServerResponse<String> insertVlineList(VLine vline, VLineList vlist) {
		int i = vlm.insert(vline);
		if (i == 0) {
			return ServerResponse.createByErrorMessage("失败");
		}
		vl.insertSelective(vlist);
		return ServerResponse.createBySuccessMessage("成功");
	}

	public ServerResponse<String> insertLineList(VLineList vl2) {
		// TODO Auto-generated method stub
		int i = vl.insertSelective(vl2);
		if (i == 0) {
			return ServerResponse.createByErrorMessage("失败");
		}
		return ServerResponse.createBySuccessMessage("成功");
	}

	public int delListById(Integer lid) {
		// TODO Auto-generated method stub
		return vl.deleteByPrimaryKey(lid);
	}

	public Map<String, Object> selectAllLineList(int pageNum, int pageSize,Integer id) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("pageNum", (pageNum-1)*pageSize);
		param.put("pageSize", pageSize);
		param.put("id", id);
		List<VLineList> list=vl.selectAllLineList(param);

		Integer count=vl.selectCount();
		Map<String,Object> resultMap=new HashMap<String,Object>();
		resultMap.put("code", 0);
		resultMap.put("msg", "查询成功");
		resultMap.put("data", list);
		resultMap.put("count", count);
		return resultMap;		
	}

	public ServerResponse<String> updateLineState(Integer lid) {
		// TODO Auto-generated method stub
		int i = vl.updateLineListState(lid);
		if (i<=0) {
			return ServerResponse.createByErrorMessage("已断开");
		}
		return ServerResponse.createBySuccessMessage("成功");
	}

	public ServerResponse<String> updateLineList(VLineList list) {
		// TODO Auto-generated method stub
		list.setState(0);
		int i = vl.updateByPrimaryKeySelective1(list);
		if (i!=0) {
			return ServerResponse.createBySuccessMessage("成功");
		}
		return ServerResponse.createByErrorMessage("失败");
	}

	public List<VLine> selectBycid(VLine vl2) {
		// TODO Auto-generated method stub
		return vlm.selectBycid(vl2);
	}

	public VLineList seleLineListByid(Integer lid) {
		// TODO Auto-generated method stub
		return (VLineList) vl.selectByPrimaryKey(lid);
	}

}
