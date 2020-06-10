package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.*;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户服务
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    //添加用户
    public void add(User user,Integer[] roleIds) {
        userDao.add(user);
        //设置用户和角色的多对多的关联关系
        Integer userId = user.getId();
        this.setUserAndRole(userId,roleIds);
    }

    //根据用户名查询数据库获取用户信息和关联的角色信息，同时需要查询角色关联的权限信息
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);//查询用户基本信息，不包含用户的角色
        if(user == null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户ID查询对应的角色
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            //根据角色ID查询关联的权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);//让角色关联权限
        }
        user.setRoles(roles);//让用户关联角色
        return user;
    }

    //用户分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);
        //select * from t_user limit 0,10
        Page<User> page = userDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<User> rows = page.getResult();
        return new PageResult(total,rows);
    }

    //根据ID删除用户
    public void deleteById(Integer id) {
        userDao.deleteAssocication(id);
        userDao.deleteById(id);
    }

    //根据用户ID查询关联的角色ID
    public List<Integer> findRoleIdsByUserId(Integer id) {
        return userDao.findRoleIdsByUserId(id);
    }

    //编辑用户
    public void edit(User user,Integer[] roleIds) {
        userDao.edit(user);
        //清理当前用户关联的角色
        userDao.deleteAssocication(user.getId());
        //        //重新建立当前用户和角色的关联关系
        Integer userId = user.getId();
        this.setUserAndRole(userId,roleIds);
    }

    //根据id查询
    public User findById(Integer id) {
        return userDao.findById(id);
    }


    //查询所有
    public List<User> findAll() {
        return userDao.findAll();
    }

    //建立用户和角色多对多关系
    public void setUserAndRole(Integer userId,Integer[] roleIds){
        if(roleIds != null && roleIds.length > 0){
            for (Integer roleId : roleIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("roleId",roleId);
                map.put("userId",userId);
                userDao.setUserAndRole(map);
            }
        }
    }

}
