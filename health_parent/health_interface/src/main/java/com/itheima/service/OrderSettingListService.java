package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSettingList;
import com.itheima.pojo.Setmeal;

import java.util.List;

public interface OrderSettingListService {
    PageResult findPage(QueryPageBean queryPageBean);

    Result add(OrderSettingList orderSettingList,Integer[] setmealIds);

    void confirm(Integer id);

    OrderSettingList findById(Integer id);

    List<Setmeal> findAllSetmeal();

    void delete(Integer id);
}
