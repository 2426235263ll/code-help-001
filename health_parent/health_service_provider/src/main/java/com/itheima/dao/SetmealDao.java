package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    List<Map<String, Object>> findSetmealCount();

    //查询所有检查组
    List<CheckGroup> findAllCheckGroup();

    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> findByCondition(String queryString);

    List<Setmeal> findAll();


    Setmeal findById(int id);

    List<Integer> findCGidsById_new2(int id);
}
