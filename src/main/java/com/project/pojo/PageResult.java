package com.project.pojo;

import java.io.Serializable;
import java.util.List;


public class PageResult implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int pageNum;
	 
	 private int pageSize;
	 
	 private long total;
	 
	 private List<?> data;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	 
	 
	 
	 
	 

}
