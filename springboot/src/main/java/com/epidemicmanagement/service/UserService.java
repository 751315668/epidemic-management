package com.epidemicmanagement.service;

import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.entity.User;

/**
 * @author XUE
 */
public interface UserService {

    User login(String account, String password);

    boolean insertUser(User user);

    User findUserById(Integer id);

    boolean updateUser(User user);

    boolean checkName(String name);

}
