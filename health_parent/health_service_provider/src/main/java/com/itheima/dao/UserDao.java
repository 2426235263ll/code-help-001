package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    public void add(User user);
    public void setUserAndRole(Map map);
    public User findByUsername(String username);
    public Page<User> selectByCondition(String queryString);
    public void deleteById(Integer id);
    public void edit(User user);
    public List<Integer> findRoleIdsByUserId(Integer id);
    public void deleteAssocication(Integer id);
    public User findById(Integer id);
    public List<User> findAll();

}
