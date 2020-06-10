package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSettingList;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderSettingListService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/orderSettingList")
public class OrderSettingListController {

    @Reference
    private OrderSettingListService orderSettingListService;


    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       return orderSettingListService.findPage(queryPageBean);
    }

    //查询所有套餐信息
    @RequestMapping("/findAllSetmeal")
    public Result findAllSetmeal(){
        try {
            List<Setmeal> list = orderSettingListService.findAllSetmeal();
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,list);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }

    //添加预约信息
    @RequestMapping("/add")
    public Result add(@RequestBody OrderSettingList orderSettingList,Integer[] setmealIds){
        try {
            String phoneNumber = orderSettingList.getPhoneNumber();
            String name = orderSettingList.getName();
            //判断名字和姓名是否为空
            if(phoneNumber.trim().length()==0||name.trim().length()==0||setmealIds.length<=0){
                    return new Result(false,MessageConstant.ORDER_FAIL);
            }
            Result result = orderSettingListService.add(orderSettingList,setmealIds);
           return result;
        } catch (Exception e) {
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

    //确认预约信息
    @RequestMapping("/confirm")
    public Result confirm(Integer id){
        try {
            //改变预约状态
            orderSettingListService.confirm(id);

            //根据id查询用户手机号
            OrderSettingList orderSettingList = orderSettingListService.findById(id);
            String phoneNumber = orderSettingList.getPhoneNumber();

            System.out.println("phoneNumber = " + phoneNumber);

            //发送短信
            System.out.println("[短信发送成功] ============== 手机号是" + phoneNumber +"取消预约.......");
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);

        } catch (Exception e) {
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/delete")
    public Result deleteGroup(Integer id){
        try {
            //根据id查询用户手机号
            OrderSettingList orderSettingList = orderSettingListService.findById(id);

            //删除预约列表数据
            orderSettingListService.delete(id);

            //再获取手机号
            String phoneNumber =orderSettingList.getPhoneNumber();
            //删除完成,再根据手机号,给用户发送短信
            System.out.println("[短信发送成功] ============== 手机号是" + phoneNumber +"取消预约.......");


            return new Result(true, MessageConstant.DELETE_ORDERLIST_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ORDERLIST_FAIL);
        }

    }

    @RequestMapping("/deleteAll")
    public Result deleteGroup(Integer[] ids){

        //批量删除预约
        if (ids != null && ids.length > 0){
            try {
                for (Integer id : ids) {
                    System.out.println("id = " + id);

                    //根据id查询用户手机号
                    OrderSettingList orderSettingList = orderSettingListService.findById(id);

                    //批量删除
                    orderSettingListService.delete(id);

                    //再获取手机号
                    String phoneNumber = orderSettingList.getPhoneNumber();
                    //删除完成,再根据手机号,给用户发送短信
                    System.out.println("[短信发送成功] ============== 手机号是" + phoneNumber+"取消预约.......");
                }
            }catch (Exception e){
                return new Result(false,MessageConstant.DELETE_ORDERLIST_FAIL);
            }
        }
        return new Result(true,MessageConstant.DELETE_ORDERLIST_SUCCESS);
    }

}
