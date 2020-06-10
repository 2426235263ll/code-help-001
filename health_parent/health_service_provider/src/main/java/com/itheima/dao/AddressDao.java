package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Address;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface AddressDao {

    //地址分页
    Page<Address> findByCondition(String queryString);

    //查询地址
    List<Address> findByCompany_address();

    //删除地址
    void deleteById(Integer id);

    //添加数据
    void add(Address address);

    //根据名称查询地址信息
    List<Address> selectByName(String company_address);
}
