package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.User;


import java.util.List;

public interface UserService {
    public void add(User user, Integer[] roleIds);
    public User findByUsername(String username);
    public PageResult pageQuery(QueryPageBean queryPageBean);
    public void deleteById(Integer id);
    public List<Integer> findRoleIdsByUserId(Integer id);
    public void edit(User user, Integer[] roleIds);
    public User findById(Integer id);
    public List<User> findAll();

}
