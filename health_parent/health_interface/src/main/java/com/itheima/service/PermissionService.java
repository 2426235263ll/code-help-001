package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Permission;


import java.util.List;

public interface PermissionService {
    public void add(Permission permission);
    public PageResult pageQuery(QueryPageBean queryPageBean);
    public void deleteById(Integer id);
    public void edit(Permission permission);
    public Permission findById(Integer id);
    public List<Permission> findAll();
}
