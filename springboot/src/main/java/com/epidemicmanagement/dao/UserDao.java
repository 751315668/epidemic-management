package com.epidemicmanagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epidemicmanagement.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author XUE
 */
@Repository
public interface UserDao extends BaseMapper<User> {
}
