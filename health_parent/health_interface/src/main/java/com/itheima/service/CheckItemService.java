package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;

//服务接口
public interface CheckItemService {

    PageResult pageQuery(QueryPageBean queryPageBean);

    public void add(CheckItem checkItem);

    public void delete(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);
}
