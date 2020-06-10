package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {
    public void add(Role role);
    public void setRoleAndPermission(Map map);
    public Set<Role> findByUserId(Integer userId);
    public Page<Role> selectByCondition(String queryString);
    public void deleteById(Integer id);
    public void edit(Role role);
    public List<Integer> findPermissionIdsByRoleId(Integer id);
    public Role findById(Integer id);
    public void deleteAssocication1(Integer id);
    public void deleteAssocication2(Integer id);
    public List<Role> findAll();
}
