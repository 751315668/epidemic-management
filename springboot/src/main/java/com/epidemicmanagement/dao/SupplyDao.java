package com.epidemicmanagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epidemicmanagement.entity.News;
import com.epidemicmanagement.entity.Supply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author XUE
 */
@Repository
public interface SupplyDao extends BaseMapper<Supply> {

    int insertSupplyVolRel(@Param(value = "supplyId") Integer supplyId,
                           @Param(value = "volId") Integer volId,
                           @Param(value = "quantity") Integer quantity);

//    Supply getTakeInfoList(Integer id);

}
