package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.OrderSettingList;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface OrderSettingListDao {

    Page<OrderSettingList> selectByCondition(String queryString);

    void update(Integer id);

    OrderSettingList findById(Integer id);

    List<Setmeal> findAllSetmeal();

    void delete(Integer id);
}
