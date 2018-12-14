package com.project.pojo;

public class VLineList {
    private Integer id;

    private Integer vlineid;

    private String ipaddress;

    private Integer state;
    
    private String vlinename;

    public String getVlinename() {
		return vlinename;
	}

	public void setVlinename(String vlinename) {
		this.vlinename = vlinename;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVlineid() {
        return vlineid;
    }

    public void setVlineid(Integer vlineid) {
        this.vlineid = vlineid;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress == null ? null : ipaddress.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}