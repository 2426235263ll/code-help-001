package com.itheima.pojo;

import java.io.Serializable;

/**
 * 地图实体类
 */
public class Address implements Serializable {

    private Integer id;//主键
    private String company_address;//公司地址
    private String longitude;//经度
    private String latitude;//维度

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", company_address='" + company_address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
