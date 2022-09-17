package com.epidemicmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.epidemicmanagement.dao.SupplyDao;
import com.epidemicmanagement.dao.SupplyTakeInfoDao;
import com.epidemicmanagement.entity.Supply;
import com.epidemicmanagement.entity.SupplyTakeInfo;
import com.epidemicmanagement.entity.Task;
import com.epidemicmanagement.entity.Volunteer;
import com.epidemicmanagement.service.SupplyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XUE
 */
@Service
@Transactional
public class SupplyServiceImpl implements SupplyService {

    private final String[] columns = {"id", "`name`", "type", "quantity", "`desc`", "left_num", "img_url", "insert_time"};

    @Autowired
    private SupplyDao supplyDao;
    @Autowired
    private SupplyTakeInfoDao supplyTakeInfoDao;

    @Override
    public boolean insertSupply(Supply supply) {
        supply.setLeftNum(supply.getQuantity());
        supply.setInsertTime(System.currentTimeMillis());
        int item = supplyDao.insert(supply);
        return item > 0;
    }

    @Override
    public PageInfo<Supply> getSupplyList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Supply> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(columns).orderByDesc("insert_time");
        List<Supply> supplyList = supplyDao.selectList(queryWrapper);
        return new PageInfo<>(supplyList);
    }

    @Override
    public PageInfo<Supply> getTypeList(Integer pageNum, Integer pageSize, String type) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Supply> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(columns).eq(StringUtils.hasText(type), "type", type).orderByDesc("insert_time");
        List<Supply> supplyList = supplyDao.selectList(queryWrapper);
        return new PageInfo<>(supplyList);
    }

    @Override
    public PageInfo<Supply> getSearchList(Integer pageNum, Integer pageSize, String name, String type) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Supply> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(columns)
                .like(StringUtils.hasText(name), "name", name)
                .eq(StringUtils.hasText(type), "type", type)
                .orderByDesc("insert_time");
        List<Supply> supplyList = supplyDao.selectList(queryWrapper);
        return new PageInfo<>(supplyList);
    }

    @Override
    public PageInfo<SupplyTakeInfo> getTakeInfoList(Integer pageNum, Integer pageSize, Integer supplyId) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<SupplyTakeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_id", supplyId).orderByAsc("insert_time");
        List<SupplyTakeInfo> infoList = supplyTakeInfoDao.selectList(queryWrapper);
        return new PageInfo<>(infoList);
    }

    @Override
    public Supply getById(Integer id) {
        Supply supply = supplyDao.selectById(id);
        return supply;
    }

    @Override
    public boolean updateSupply(Supply supply) {
        Supply oldSup = supplyDao.selectById(supply.getId());
        Integer oldQuantity = oldSup.getQuantity(); // 未更新的总数
        Integer curQuantity = supply.getQuantity(); // 更新后的总数
        Integer newCnt =  curQuantity - oldQuantity; // 新增加的数量
        Integer leftNum = supply.getLeftNum();
        if (leftNum + newCnt >= 0) {
            supply.setLeftNum(supply.getLeftNum() + newCnt);
        } else {
            supply.setLeftNum(0);
        }
        int item = supplyDao.updateById(supply);
        return item > 0;
    }

    // 物资取用
    @Override
    public boolean insertSupplyVolRel(SupplyTakeInfo info) {
        Supply supply = supplyDao.selectById(info.getSupId());
        supply.setLeftNum(supply.getLeftNum() - info.getCnt());
        supplyDao.updateById(supply);
        int item = supplyTakeInfoDao.insert(info);
//        int item = supplyDao.insertSupplyVolRel(supplyId, volId, quantity);
        return item > 0;
    }

    @Override
    public List<Integer> getSupplyChartData() {
        QueryWrapper<Supply> wrapper = new QueryWrapper<>();
        wrapper.select("id", "type", "left_num");
        List<Supply> supplies = supplyDao.selectList(wrapper);
        int i = 0, j = 0, x = 0, y = 0, n = 0, m = 0;// 【口罩类、服装穿戴类、电子仪器类、消毒用品类、食品类、其他】
        for (Supply s : supplies) {
            switch (s.getType()) {
                case "口罩类":
                    i += s.getLeftNum();
                    break;
                case "服装穿戴类":
                    j += s.getLeftNum();
                    break;
                case "电子仪器类":
                    x += s.getLeftNum();
                    break;
                case "消毒用品类":
                    y += s.getLeftNum();
                    break;
                case "食品类":
                    n += s.getLeftNum();
                    break;
                case "其他":
                    m += s.getLeftNum();
                    break;
            }
        }
        List<Integer> res = new ArrayList<>(6);
        res.add(i);
        res.add(j);
        res.add(x);
        res.add(y);
        res.add(n);
        res.add(m);
        return res;
    }

    @Override
    public Integer getSize() {
        QueryWrapper<Supply> wrapper = new QueryWrapper<>();
        wrapper.select("id", "left_num");
        List<Supply> supplies = supplyDao.selectList(wrapper);
        Integer res = 0;
        for (Supply s : supplies) {
            res += s.getLeftNum();
        }
        return res;
    }
}
