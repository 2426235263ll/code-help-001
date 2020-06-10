package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.dao.OrderSettingListDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.*;
import com.itheima.service.OrderSettingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingListService.class)
@Transactional
public class OrderSettingListServiceImpl implements OrderSettingListService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingListDao orderSettingListDao;


    //查询预约信息
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        Integer currentPage = queryPageBean.getCurrentPage();

        //使用mybaties分页助手完成分页查询
        PageHelper.startPage(currentPage, pageSize);
        Page<OrderSettingList> page =orderSettingListDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<OrderSettingList> rows = page.getResult();
        return new PageResult(total,rows);
    }


    //添加电话预约
    @Override
    public Result add(OrderSettingList orderSettingList,Integer[] setmealIds) {
        //1.检查当前日期是否设置了预约,如果没有则不能预约
        Date orderDate = orderSettingList.getOrderDate();
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2.检查当前日期是否预约满
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations >= number) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }


        //3.判断是否重复预约,同一用户同一天预约了同一个套餐,如果重复预约则不能再预约
        String telephone = orderSettingList.getPhoneNumber();
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            //如果存在该会员信息
            Date order_date = orderDate;//预约日期
            Integer memberId = member.getId();//会员id

            for (Integer setmealId : setmealIds) {
                Order order = new Order(memberId, order_date,setmealId);
                //根据条件查询
                List<Order> list = orderDao.findByCondition(order);
                if(list!=null&&list.size()>0){
                    //重复预约
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            }


        }else {
            //如果不存在会员信息,先进行用户注册
            member = new Member();
            member.setName(orderSettingList.getName());
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setFileNumber(orderSettingList.getFileNumber());
            memberDao.add(member);//自动完成会员注册
        }

        //保存用户预约信息
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员ID
        order.setOrderDate(orderDate);//预约日期
        order.setOrderType(orderSettingList.getOrderType());//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态

        //设置会员套餐id
        if(setmealIds!=null&&setmealIds.length>0){
            for (Integer setmealId : setmealIds) {
                order.setSetmealId(setmealId);
            }
        }
        //添加用户信息
        orderDao.add(order);

        //改变预约人数
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //确认预约状态
    @Override
    public void confirm(Integer id) {

        orderSettingListDao.update(id);
    }

    //根据id查询预约信息(封装进自定义pojo类)
    @Override
    public OrderSettingList findById(Integer id) {
        return orderSettingListDao.findById(id);
    }

    //查询所有的套餐信息
    @Override
    public List<Setmeal> findAllSetmeal() {
        return orderSettingListDao.findAllSetmeal();
    }

    //删除预约信息
    @Override
    public void delete(Integer id) {
        //根据id查询用户预约信息
        OrderSettingList orderSettingList = orderSettingListDao.findById(id);
        Date orderDate = orderSettingList.getOrderDate();
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);

        //改变预约人数
        orderSetting.setReservations(orderSetting.getReservations()-1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        //删除用户预约信息
        orderSettingListDao.delete(id);


    }

}
