package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Address;

import java.util.List;

/**
 * 地图服务接口
 */

public interface AddressService {

    //地图分页数据
    PageResult findPageAll(Integer currentPage, Integer pageSize, String queryString);
    //查询地图
    List<Address> findByCompany_address();
    //更具id删除地图
    void deleteById(Integer id);
    //添加地图数据
    void add(Address address);

    //根据名称查询地址
    boolean selectByName(String company_address);

}
