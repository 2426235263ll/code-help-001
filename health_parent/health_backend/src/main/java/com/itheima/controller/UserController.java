package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    //获得当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当Spring security完成认证后，会将当前用户信息保存到框架提供的上下文对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);

        if(user != null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }

        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }

    //添加数据
    @RequestMapping("/add")
    public Result add(@RequestBody User user,Integer[] roleIds){
        try {
            String name = user.getName();
            //判断用户姓名和角色是否为空
            if(name.trim().length()==0||roleIds.length<=0){
                return new Result(false,MessageConstant.ORDER_FAIL);
            }
            userService.add(user,roleIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
        return new Result(true,MessageConstant.ADD_USER_SUCCESS);
    }


    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = userService.pageQuery(queryPageBean);
        return pageResult;
    }


    //删除数据
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        System.out.println(id);
        try {
            userService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_USER_FAIL);
        }

        return new Result(true,MessageConstant.DELETE_USER_SUCCESS);
    }

    //根据角色ID查询用户ID
    @RequestMapping("/findRoleIdsByUserId")
    public Result findRoleIdsByUserId(Integer id) {
        try {
            List<Integer> roleIds = userService.findRoleIdsByUserId(id);
            return new Result(true, MessageConstant.QUERY_USER_SUCCESS, roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
    }


    //编辑用户
    @RequestMapping("/edit")
    public Result edit(@RequestBody User user,Integer[] roleIds) {
        try {
            userService.edit(user,roleIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_USER_FAIL);
        }

        return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
    }

    //根据ID查询用户
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            User user=userService.findById(id);
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,user);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }

    }

    //查询所有
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<User> list=userService.findAll();
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }

    }


}
