package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 预约设置服务
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class  OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    //批量导入预约设置数据
    @Override
    public void add(List<OrderSetting> list) {
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                //判断当前日期是否已经进行了预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());

                if (countByOrderDate > 0) {//存在执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    //执行添加操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据月份查询对应的预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String begin = date + "-1";//2019-6-1
        String end = date + "-31";//2019-6-31
        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String, Object> m = new HashMap<>();
            m.put("date", orderSetting.getOrderDate().getDate());
            m.put("number", orderSetting.getNumber());
            m.put("reservations", orderSetting.getReservations());
            result.add(m);
        }
        return result;
    }

    //根据日期设置对应的预约设置数据
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //判断当前日期是否已经进行了预约设置
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());

        if (count > 0) {//存在执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //执行添加操作
            orderSettingDao.add(orderSetting);
        }
    }
}