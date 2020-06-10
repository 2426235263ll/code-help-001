package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Address;
import com.itheima.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图
 */

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    /**
     * 地图数据分页
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        System.out.println(queryPageBean.getQueryString());
        //传递分页参数
        PageResult pageResult = addressService.findPageAll(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 查询所有地址名称
     */
    @RequestMapping("/findByCompany_address")
    public Result findByCompany_address(){
        try {
            //查询所有地址名称
            List<Address> list = addressService.findByCompany_address();

            //创建一个数组接受地址名称数据
            List<String> nameList = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (Address address : list) {
                    nameList.add(address.getCompany_address());
                }
            }
            System.out.println("nameList = " + nameList.toArray());
            return new Result(true,MessageConstant.QUERY_ADDRESS_SUCCESS,nameList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ADDRESS_FAIL);
        }
    }

    /**
     * 根据id删除地址
     */
    @RequestMapping("/delete")
    public Result deleteById(Integer id){
        try {
            //调用方法根据id删除
            addressService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();

        }
        //删除成功
        return new Result(true,MessageConstant.DELETE_ADDRESS_SUCCESS);
    }

    /**
     * 添加地址
     * @param
     * @return
     */
    @RequestMapping("/add")
    public Result deleteById(@RequestBody Address address){

        try {
            //名称
            String company_address = address.getCompany_address();
            //经度
            String longitude = address.getLongitude();
            //维度
            String latitude = address.getLatitude();

            //判断地址是否输入不正确
            if (company_address == null || company_address.length() == 0) {
                //不正确直接返回错误信息
                return new Result(false, MessageConstant.ADD_ADDRESS_FAIL);
            }

            //查询地址是否存在
            boolean flag = addressService.selectByName(address.getCompany_address());
//            System.out.println(flag);
            if (flag){
                //地址已存在
                return new Result(false,MessageConstant.ADD_ADDRESS_ALREADY_EXIST);
            }


            //调用方法添加数据
            addressService.add(address);
            //添加成功
            return new Result(true,MessageConstant.ADD_ADDRESS_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_ADDRESS_FAIL);
        }
    }

}
