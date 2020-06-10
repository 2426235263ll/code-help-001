package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Role;

import java.util.List;

public interface RoleService {
    public void add(Role role, Integer[] permissionIds);
    public PageResult pageQuery(QueryPageBean queryPageBean);
    public List<Role> findAll();
    public Role findById(Integer id);
    public void deleteById(Integer id);
    public List<Integer> findPermissionIdsByRoleId(Integer id);
    public void edit(Role role, Integer[] permissionIds);
}
