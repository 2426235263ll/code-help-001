package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体检套餐服务
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private  CheckGroupDao checkGroupDao;

    @Autowired
    private CheckItemDao checkItemDao;
    //查询所有检查组
    @Override
    public List<CheckGroup> findAllCheckGroup() {
        return setmealDao.findAllCheckGroup();
    }

    //新增套餐
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //先添加套餐数据
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        if(checkgroupIds != null && checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
        //将图片名称保存在redis集合中
        String fileName = setmeal.getImg();
        System.out.println("fileName = " + fileName);
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }

    //分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(int id) {
        //1.根据套餐id查询套餐信息
        Setmeal setmeal = setmealDao.findById(id);

        // 2. 根据套餐id查询套餐_检查组表关联的 检查组ids
        List<Integer> cGIds = setmealDao.findCGidsById_new2(id);

        //存放每个检查套餐中，所有检查组信息的list
        List<CheckGroup> cGList = new ArrayList();

        for (Integer cgId : cGIds) {

            //存放每个检查组中，所有检查项信息的list
            List<CheckItem> cIList = new ArrayList();

            //3.根据检查组id，查询检查组信息
            CheckGroup cg = checkGroupDao.findById(cgId);

            //4.根据检查组id，查询检查组_检查项关联表的 检查项ids
            List<Integer> ciIDList = checkGroupDao.findCheckItemIdsByCheckGroupId(cg.getId());

            for (Integer ciID : ciIDList) {
                //5.根据检查项id，查询检查项信息
                CheckItem checkItem = checkItemDao.findById(ciID);
                cIList.add(checkItem);
            }
            cg.setCheckItems(cIList);
            cGList.add(cg);
        }
        setmeal.setCheckGroups(cGList);

        return setmeal;

    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
