package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;

public interface CheckItemDao {

    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(String queryString);

    void delete(Integer id);

    long findCountByCheckItemId(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);
}
