package com.epidemicmanagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epidemicmanagement.entity.News;
import com.epidemicmanagement.entity.Volunteer;
import org.springframework.stereotype.Repository;

/**
 * @author XUE
 */
@Repository
public interface VolunteerDao extends BaseMapper<Volunteer> {

    int deleteTaskInfo(Integer volId);

    Integer getTaskIdByVolId(Integer volId);

}
