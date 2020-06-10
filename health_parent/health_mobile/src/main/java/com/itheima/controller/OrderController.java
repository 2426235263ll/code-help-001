package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        //从redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");

        //将用户输入的验证码个redis保存中的进行对比
        if(validateCodeInRedis != null && validateCode!=null&&validateCode.equals(validateCodeInRedis)){
            //如果验证码一致,调用服务层进行预约业务处理
            map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型
            Result result = null;

            try {
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();

                //如果没有查询到结果,表示异常提醒重试
                if(result==null){
                    result = new Result(false,MessageConstant.ORDER_FAIL);
                }
                return result;
            }

            if(result.isFlag()){
                //预约成功,发送成功短信
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, (String) map.get("orderDate"));
                    System.out.println("[已经预约成功]-------手机号是:"+telephone+"预约日期是:"+(String) map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }else {
            //如果验证码不一致,返回结果给页面
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}

