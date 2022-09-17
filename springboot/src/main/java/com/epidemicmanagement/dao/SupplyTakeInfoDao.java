package com.epidemicmanagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epidemicmanagement.entity.Supply;
import com.epidemicmanagement.entity.SupplyTakeInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author XUE
 */
@Repository
public interface SupplyTakeInfoDao extends BaseMapper<SupplyTakeInfo> {


}
