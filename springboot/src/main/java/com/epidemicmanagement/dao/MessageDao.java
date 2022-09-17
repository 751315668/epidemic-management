package com.epidemicmanagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epidemicmanagement.entity.Message;
import com.epidemicmanagement.entity.News;
import org.springframework.stereotype.Repository;

/**
 * @author XUE
 */
@Repository
public interface MessageDao extends BaseMapper<Message> {
}
