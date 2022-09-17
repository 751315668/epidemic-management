package com.epidemicmanagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epidemicmanagement.entity.News;
import com.epidemicmanagement.entity.Task;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author XUE
 */
@Repository
public interface TaskDao extends BaseMapper<Task> {

    int insertTaskVolRel(@Param(value = "taskId") Integer taskId, @Param(value = "volId") Integer volId);

    Task getParticipantList(Integer id);

}
