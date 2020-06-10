package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;

    //添加数据
    @RequestMapping("/add")
    public Result add(@RequestBody Role role,Integer[] permissionIds){
        try {
            String name = role.getName();
            //判断角色名和权限是否为空
            if(name.trim().length()==0||permissionIds.length<=0){
                return new Result(false,MessageConstant.ADD_ROLE_FAIL);
            }

            roleService.add(role,permissionIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
        return new Result(true,MessageConstant.ADD_ROLE_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = roleService.pageQuery(queryPageBean);
        return pageResult;
    }

    //查询所有
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Role> list=roleService.findAll();
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }

    }

    //根据id删除
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        System.out.println(id);
        try {
            roleService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ROLE_FAIL);
        }

        return new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
    }

    //根据id查询
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Role role=roleService.findById(id);
            return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,role);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }

    }

    @RequestMapping("/findPermissionIdsByRoleId")
    public Result findPermissionIdsByRoleId(Integer id) {
        try {
            List<Integer> permissionIds = roleService.findPermissionIdsByRoleId(id);
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, permissionIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    //编辑
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role,Integer[] permissionIds) {
        try {
            roleService.edit(role,permissionIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ROLE_FAIL);
        }

        return new Result(true,MessageConstant.EDIT_ROLE_SUCCESS);
    }
}
