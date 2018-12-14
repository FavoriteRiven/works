package com.project.dao;

import com.project.pojo.Code;

public interface CodeMapper extends BaseMapper<Code>{
    
    int getCode(String code);
    
}