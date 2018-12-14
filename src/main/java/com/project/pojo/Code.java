package com.project.pojo;

import java.util.Date;

public class Code {
	private int mId;
	private String code;
	private Date validity;
	
	public Code(int mId, String code, Date validity) {
		super();
		this.mId = mId;
		this.code = code;
		this.validity = validity;
	}
	
	public Code() {
		super();
	}

	public Code(String code, Date validity) {
		super();
		this.code = code;
		this.validity = validity;
	}

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getValidity() {
		return validity;
	}
	public void setValidity(Date validity) {
		this.validity = validity;
	}
	
	
}
