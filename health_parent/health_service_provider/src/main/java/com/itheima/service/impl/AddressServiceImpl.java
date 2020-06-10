package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.AddressDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Address;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地图业务层
 */
@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceImpl implements AddressService {


    @Autowired
    private AddressDao addressDao;

    //地图分页
    @Override
    public PageResult findPageAll(Integer currentPage, Integer pageSize, String queryString) {
        //分页工具
        PageHelper.startPage(currentPage,pageSize);
        //分页条件,公司地址
        Page<Address> page = addressDao.findByCondition(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    //地址名称
    @Override
    public List<Address> findByCompany_address() {
        //查询所有地址名称
        return addressDao.findByCompany_address();
    }

    //删除地址
    @Override
    public void deleteById(Integer id) {
        //根据id删除地址
        addressDao.deleteById(id);
    }

    //添加地址
    @Override
    public void add(Address address) {
        //添加地址
        addressDao.add(address);
    }

    //根据名称查询地址
    @Override
    public boolean selectByName(String company_address) {
        //查询用户是否存在
        List<Address> list = addressDao.selectByName(company_address);
        //存在
        if (list != null && list.size() > 0){
//            System.out.println(list);
            return true;
        }
        //不存在
        return false;
    }
}
