package com.project.pojo;

import java.util.Date;

public class VUser {
    private Integer id;

    private String userName;

    private String passWord;

    private String phone;

    private Date createTime;

    private Date updateTime;

    private Byte isfilter;

    private Byte ismember;

    private Date rechargeTime;

    private Date expireTime;

    private String openid;

    private Byte islogin;

    private Byte userType;

    private Integer currentConnection;
    
    private String network;
    
    private Integer maxlogin;
    
    private String pcExpireTime;

    public String getPcExpireTime() {
		return pcExpireTime;
	}

	public void setPcExpireTime(String valueOf) {
		this.pcExpireTime = valueOf;
	}

	public Integer getMaxlogin() {
		return maxlogin;
	}

	public void setMaxlogin(Integer maxlogin) {
		this.maxlogin = maxlogin;
	}
    
    public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord == null ? null : passWord.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getIsfilter() {
        return isfilter;
    }

    public void setIsfilter(Byte isfilter) {
        this.isfilter = isfilter;
    }

    public Byte getIsmember() {
        return ismember;
    }

    public void setIsmember(Byte ismember) {
        this.ismember = ismember;
    }

    public Date getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public Byte getIslogin() {
        return islogin;
    }

    public void setIslogin(Byte islogin) {
        this.islogin = islogin;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public Integer getCurrentConnection() {
        return currentConnection;
    }

    public void setCurrentConnection(Integer currentConnection) {
        this.currentConnection = currentConnection;
    }
}