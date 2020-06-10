package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService{
    @Autowired
    private PermissionDao permissionDao;
    //新增
    public void add(Permission permission) {
        permissionDao.add(permission);
    }


    //分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Permission> page = permissionDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //查询所有
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }

    //根据ID删除权限
    public void deleteById(Integer id) {
        permissionDao.deleteAssocication(id);
        permissionDao.deleteById(id);
    }


    //编辑
    public void edit(Permission permission) {
        permissionDao.edit(permission);
        //清理当前角色关联的权限
        permissionDao.deleteAssocication(permission.getId());

    }

    //根据id查询
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

}
