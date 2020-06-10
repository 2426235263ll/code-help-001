package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Reference
    private PermissionService permissionService;

    //插入数据
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission){

        try {
            String permissionName = permission.getName();

            //判断添加的权限名是否为空
            if(permissionName.trim().length()==0||permissionName==null){
                return new Result(false,MessageConstant.ADD_PERMISSION_FAIL);
            }
            permissionService.add(permission);
            return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }

    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return permissionService.pageQuery(queryPageBean);
    }

    //查询所有
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Permission> list=permissionService.findAll();
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }

    }

    //根据id删除
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        System.out.println(id);
        try {
            permissionService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_PERMISSION_FAIL);
        }

        return new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

    //编辑
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission) {
        try {
            permissionService.edit(permission);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_PERMISSION_FAIL);
        }

        return new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
    }

    //根据ID查询
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Permission permission=permissionService.findById(id);
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }

    }
}
