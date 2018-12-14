package com.project.pojo;

public class VCity {
    private Integer id;

    private Integer vProvinceId;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getvProvinceId() {
        return vProvinceId;
    }

    public void setvProvinceId(Integer vProvinceId) {
        this.vProvinceId = vProvinceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}