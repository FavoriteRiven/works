package com.project.controller.portal;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.common.ServerResponse;
import com.project.pojo.VLine;
import com.project.pojo.VLineList;
import com.project.pojo.VUser;
import com.project.service.VLineService;
import com.project.service.VUserService;
import com.project.util.PropertiesUtil;
@CrossOrigin
@Controller
@RequestMapping("/line/")
public class VLineController {

    @Autowired
    private VLineService iVLineService;
    
    @Autowired
    private VUserService iVUserService;
    
    private static String maxcollection = PropertiesUtil.getProperty("max.collection");

    /**
     * 查询所有动态线路
     * @return
     */
    @RequestMapping(value = "alldynamic",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<VLine>> se_line(Integer cityId){
    	if(cityId!=null){
    		VLine vl = new VLine();
        	vl.setCityId(cityId);
        	vl.setType("0");
        	List<VLine> list = iVLineService.selectBycid(vl);
        	return ServerResponse.createBySuccess("成功", list);
    	}
        ServerResponse<List<VLine>> response = iVLineService.selectAlldynamic();
        return response;
    }
    
    
    /**
     * 查询所有静态线路
     * @return
     */
    @RequestMapping(value = "allstatic",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<VLine>> se_line_static(Integer cityId){
    	if(cityId!=null){
    		VLine vl = new VLine();
        	vl.setCityId(cityId);
        	vl.setType("1");
        	List<VLine> list = iVLineService.selectBycid(vl);
        	return ServerResponse.createBySuccess("成功", list);
    	}
        ServerResponse<List<VLine>> response = iVLineService.selectAllstatic();        
        return response;
    }
    
    /**
     * 链接线路
     * @return
     */
    @RequestMapping(value = "connectionip/{uid}/{line_id}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> line_connection(@PathVariable("uid") Integer uid,@PathVariable("line_id") Integer line_id){
    	
    	VLine vline = (VLine) iVLineService.selectById(line_id);//判断当前要链接IP的连接数是否超载
    	if(vline.getConnectionNum()!=null){
    		if(vline.getConnectionNum()>=Integer.valueOf(maxcollection)){
        		return ServerResponse.createByErrorMessage("此线路当前连接人数过多，建议您换条线路");
        	}
    	}
    	
    	VUser user = (VUser) iVUserService.selectById(uid);//查询当前用户最后一次链接的IPid
    	Integer currentLineId = user.getCurrentConnection();
    	iVLineService.updateByLineId(currentLineId);//上一条IP连接人数-1
    	iVLineService.updateByLineIdAdd(line_id);//当前链接的IP连接人数+1
    	
    	iVUserService.updateLineIp(line_id,uid);//修改改变的IPid
    	
    	return ServerResponse.createBySuccessMessage("yes");
    }
    
    /**
     * 链接线路
     * @param uid
     * @param lid
     * @return
     */
    @RequestMapping(value = "connection/{uid}/{lid}/{beforeid}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<VLineList> connection(@PathVariable("uid") Integer uid,@PathVariable("lid") Integer lid,@PathVariable("beforeid") Integer beforeid){
    	VUser user = (VUser) iVUserService.selectById(uid);
    	Date date = new Date();
    	if(user==null){
    		return ServerResponse.createByErrorCodeMessage(5,"未登录");
    	}
    	if(user.getExpireTime()==null){
    		return ServerResponse.createByErrorCodeMessage(2,"您还不是会员,\n是否需要充值?");
    	}
    	if (user.getExpireTime().getTime()<date.getTime()) {
			return ServerResponse.createByErrorCodeMessage(2,"你的账户已到期,\n是否需要充值?");
		}
    	
    	VLineList vlist = iVLineService.selectLineList(lid,beforeid);
    	if(vlist==null){
    		return ServerResponse.createByErrorCodeMessage(9,"当前线路连接人数已满");
    	}
    	iVLineService.updateByLineListId(vlist.getId());
    	VUser vuser = new VUser();
    	vuser.setId(uid);
    	vuser.setIslogin((byte) 1);
    	iVUserService.updateByKey(vuser);
		return ServerResponse.createBySuccess("正常使用", vlist);
    }
    
    
    /**
     * 断开链接
     * @param lid
     * @return
     */
    @RequestMapping(value = "breakline/{lid}/{uid}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> breakline(@PathVariable("lid") Integer lid,@PathVariable("uid") Integer uid){
    	VUser user = new VUser();
    	user.setId(uid);
    	user.setIslogin((byte) 0);
    	iVUserService.updateByKey(user);
    	return iVLineService.updateLineState(lid);
    }
    
    /**
     * 根据城市获取线路
     * @param cid
     * @return
     */
    @RequestMapping(value = "getline/{cid}/{state}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<VLine>> getline(@PathVariable("cid") Integer cid,@PathVariable("state") String state){
    	VLine vl = new VLine();
    	vl.setCityId(cid);
    	vl.setType(state);
    	List<VLine> list = iVLineService.selectBycid(vl);
    	return ServerResponse.createBySuccess("成功", list);
    }
    
}
