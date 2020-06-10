package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;

    //添加角色
    public void add(Role role,Integer[] permissionIds) {
        roleDao.add(role);
        //设置角色和权限的多对多的关联关系
        Integer roleId = role.getId();
        this.setRoleAndPermission(roleId,permissionIds);
    }

    //检查项分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);
        //select * from t_ limit 0,10
        Page<Role> page = roleDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<Role> rows = page.getResult();
        return new PageResult(total,rows);
    }

    //根据ID删除角色
    public void deleteById(Integer id) {
        roleDao.deleteAssocication1(id);
        roleDao.deleteAssocication2(id);
        roleDao.deleteById(id);
    }


    //根据角色ID查询关联的权限ID
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        return roleDao.findPermissionIdsByRoleId(id);
    }

    //
    public void edit(Role role,Integer[] permissionIds) {
        roleDao.edit(role);
        //清理当前角色关联的权限，操作中间关系表
        roleDao.deleteAssocication1(role.getId());

        //重新建立当前角色和权限的关联关系
        Integer roleId = role.getId();
        this.setRoleAndPermission(roleId,permissionIds);
    }

    //根据id查询
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    public List<Role> findAll() {
        return roleDao.findAll();
    }

    //建立角色和权限多对多关系
    public void setRoleAndPermission(Integer roleId,Integer[] permissionIds){
        if(permissionIds != null && permissionIds.length > 0){
            for (Integer permissionId : permissionIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("roleId",roleId);
                map.put("permissionId",permissionId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }
}
