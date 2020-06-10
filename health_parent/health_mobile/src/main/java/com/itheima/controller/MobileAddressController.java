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
@RequestMapping("/mobileAddress")
public class MobileAddressController {

    @Reference
    private AddressService addressService;

    /**
     * 微信端查询所有地址名称
     */
    @RequestMapping("/findByCompany_address")
    public Result findByCompany_address(){
        try {
            //查询所有地址
            List<Address> list = addressService.findByCompany_address();

            return new Result(true,MessageConstant.QUERY_ADDRESS_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ADDRESS_FAIL);
        }
    }


}
