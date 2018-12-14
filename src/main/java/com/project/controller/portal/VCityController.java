package com.project.controller.portal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.common.ServerResponse;
import com.project.pojo.VCity;
import com.project.pojo.VProvince;
import com.project.service.VCityService;
import com.project.service.VProvinceService;
@CrossOrigin
@Controller
@RequestMapping("/city/")
public class VCityController {

    @Autowired
    private VProvinceService iVProvinceService;
    
    @Autowired
    private VCityService iVCityService;

    /**
     * 查询省市
     * @return
     */
    @RequestMapping(value = "province",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<VProvince>> se_province(){
    	
        ServerResponse<List<VProvince>> response = iVProvinceService.selectAll();
        
        return response;
    }
    
    /**
     * 查询城市
     * @return
     */
    @RequestMapping(value = "city/{cId}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<VCity>> se_city(@PathVariable("cId") int cId){
    	
        ServerResponse<List<VCity>> response = iVCityService.selectByProvinceId(cId);
        
        return response;
    }

}
