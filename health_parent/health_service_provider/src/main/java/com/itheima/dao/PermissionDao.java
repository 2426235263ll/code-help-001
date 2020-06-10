package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PermissionDao {
    public Set<Permission> findByRoleId(Integer roleId);
    public void setPermissionAndRole(Map map);
    public void add(Permission permission);
    public Page<Permission> findByCondition(String queryString);
    public Page<Permission> selectByCondition(String queryString);
    public void deleteById(Integer id);
    public void edit(Permission permission);
    public void deleteAssocication(Integer id);
    public Permission findById(Integer id);

    public List<Permission> findAll();
}
