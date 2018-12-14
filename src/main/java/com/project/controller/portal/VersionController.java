package com.project.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.common.ServerResponse;
import com.project.pojo.VVersion;
import com.project.service.VersionService;
@CrossOrigin
@Controller
@RequestMapping("/version/")
public class VersionController {
	
	@Autowired
	private VersionService iVersionService;
	

	@RequestMapping(value = "versioninfo/{apptype}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<VVersion> versioninfo(@PathVariable("apptype") Integer apptype){
    	
        ServerResponse<VVersion> response = iVersionService.selectByKey(apptype);
        return response;
    }
	
}
