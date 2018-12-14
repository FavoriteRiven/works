package com.project.pojo;

import java.math.BigDecimal;

public class VMember {
    private Integer id;

    private String title;

    private BigDecimal price;

    private BigDecimal costprice;

    private String company;
    
    private String companyRem;

	private String describe1;

    private String describe2;

    private String describe3;
    
    private Integer maxlogin;
    
    public String getCompanyRem() {
		return companyRem;
	}

	public void setCompanyRem(String companyRem) {
		this.companyRem = companyRem;
	}

    public Integer getMaxlogin() {
		return maxlogin;
	}

	public void setMaxlogin(Integer maxlogin) {
		this.maxlogin = maxlogin;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCostprice() {
        return costprice;
    }

    public void setCostprice(BigDecimal costprice) {
        this.costprice = costprice;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public String getDescribe1() {
        return describe1;
    }

    public void setDescribe1(String describe1) {
        this.describe1 = describe1 == null ? null : describe1.trim();
    }

    public String getDescribe2() {
        return describe2;
    }

    public void setDescribe2(String describe2) {
        this.describe2 = describe2 == null ? null : describe2.trim();
    }

    public String getDescribe3() {
        return describe3;
    }

    public void setDescribe3(String describe3) {
        this.describe3 = describe3 == null ? null : describe3.trim();
    }
}