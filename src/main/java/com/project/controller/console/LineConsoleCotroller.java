package com.project.controller.console;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.project.common.ServerResponse;
import com.project.pojo.VLine;
import com.project.pojo.VLineList;
import com.project.service.VLineService;

/**
 * @author Administrator
 * 后台线路管理
 *
 */
@CrossOrigin
@Controller
@RequestMapping("/consoleline/")
public class LineConsoleCotroller {

	@Autowired
	private VLineService iVLineService;
	
	/**
	 * 获取后台线路列表
	 * @return
	 */
	@RequestMapping(value="info",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getinfo(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, 
			@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
		Map<String,Object> resultMap=iVLineService.selectAllLine(pageNum,pageSize);
		return resultMap;
	}
	
	/**
	 * 修改线路信息
	 * @param vl
	 * @return
	 */
	@RequestMapping(value = "update",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> updateline(VLine vl){

		return iVLineService.updateLIine(vl);
	}
	
	/**
	 * 添加线路信息
	 * @param vl
	 * @return
	 */
	@RequestMapping(value = "insert",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> insertLine(VLine vl){

		
		return iVLineService.insert(vl);
		//return iVLineService.insert(vl);		
	}
	
	/**
	 * 删除线路信息
	 * @param lid
	 * @return
	 */
	@RequestMapping(value = "delete/{lid}",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> deleteLine(@PathVariable("lid") Integer lid){
		int i = iVLineService.delById(lid);
		if (i==1) {
			return ServerResponse.createBySuccessMessage("删除成功");
		}
		return ServerResponse.createBySuccessMessage("删除失败");
	}
	
	
	
	/**
	 * 获取后台线路配置列表
	 * @return
	 */
	@RequestMapping(value="listinfo",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> lsitinfo(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, 
			@RequestParam(value = "pageSize",defaultValue = "10") int pageSize,Integer id){
		Map<String,Object> resultMap=iVLineService.selectAllLineList(pageNum,pageSize,id);
		return resultMap;
	}
	
	
	
	/**
	 * 断开线路配置
	 * @param list
	 * @return
	 */
	@RequestMapping(value="setlinelist",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> setLinList(VLineList list){
		return iVLineService.updateLineList(list);
	}
	
	
	
	/**
	 * 一键断开所有配置
	 * @param list
	 * @return
	 */
	@RequestMapping(value="setallline",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> setAllLinList(VLineList list){
		return iVLineService.updateLineList(list);
	}
	
	
	
	
	/**
	 * 添加线路配置列表
	 * @param vl
	 * @return
	 */
	@RequestMapping(value = "insert/linelist",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> insertLineList(@RequestParam("vlineid")Integer lid,@RequestParam("file") MultipartFile file,HttpServletRequest request){

		
		if(!file.isEmpty()){
			try {
				System.out.println(lid);
				String filename = System.currentTimeMillis()+ file.getOriginalFilename();
				String savedDir = request.getSession().getServletContext().getRealPath("lineconfig");
				String contentType=file.getContentType();  
	            //获得文件后缀名称  
	            String endName=contentType.substring(contentType.indexOf("/")+1);
	            String path = "/"+filename;
				File newFile=new File(savedDir+path);
				if(!newFile.exists()) {       
		            newFile.mkdirs();
		        } 
				file.transferTo(newFile);
				
				VLineList vlist = new VLineList();
				
				vlist.setVlineid(lid);
				vlist.setIpaddress("/lineconfig"+path);
				vlist.setState(0);
				
				return iVLineService.insertLineList(vlist);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ServerResponse.createByErrorMessage("文件不能为空");
		//return iVLineService.insert(vl);		
	}
	
	/**
	 * 删除线路配置信息
	 * @param lid
	 * @return
	 */
	@RequestMapping(value = "deletelist/{lid}",method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> deleteLineList(@PathVariable("lid") Integer lid,HttpServletRequest request){
		
		VLineList list = iVLineService.seleLineListByid(lid);
		String name = list.getIpaddress();
		String savedDir = request.getSession().getServletContext().getRealPath("");
		String filename = savedDir+name;
		System.out.println(filename);
		File file = new File(filename);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + filename + "成功！");
                iVLineService.delListById(lid);
                return ServerResponse.createBySuccessMessage("删除成功");
            } else {
                System.out.println("删除单个文件" + filename + "失败！");
                return ServerResponse.createByErrorMessage("删除失败");
            }
        } else {
        	 return ServerResponse.createByErrorMessage("删除失败,文件不存在");
        }
	}
	
}
