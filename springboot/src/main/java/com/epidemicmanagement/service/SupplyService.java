package com.epidemicmanagement.service;

import com.epidemicmanagement.entity.News;
import com.epidemicmanagement.entity.Supply;
import com.epidemicmanagement.entity.SupplyTakeInfo;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author XUE
 */
public interface SupplyService {

    boolean insertSupply(Supply supply);

    PageInfo<Supply> getSupplyList(Integer pageNum, Integer pageSize);

    PageInfo<Supply> getTypeList(Integer pageNum, Integer pageSize, String type);

    PageInfo<Supply> getSearchList(Integer pageNum, Integer pageSize, String name, String type);

    PageInfo<SupplyTakeInfo> getTakeInfoList(Integer pageNum, Integer pageSize, Integer supplyId);

    Supply getById(Integer id);

    boolean updateSupply(Supply supply);

    boolean insertSupplyVolRel(SupplyTakeInfo info);

    List<Integer> getSupplyChartData();

    Integer getSize();

}
