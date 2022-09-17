package com.epidemicmanagement.service;

import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.entity.Volunteer;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author XUE
 */
public interface VolunteerService {

    PageInfo<Volunteer> getVolunteerList(Integer pageNum, Integer pageSize);

    PageInfo<Volunteer> searchVolunteerList(Integer pageNum, Integer pageSize, String param);

    PageInfo<Volunteer> getApplyList(Integer pageNum, Integer pageSize);

    boolean insertVolunteer(Volunteer volunteer);

    boolean acceptApply(Integer id);

    boolean denyApply(Integer id);

    boolean updateVolunteer(Volunteer volunteer);

    Volunteer findByUserId(Integer id);

    Volunteer findById(Integer id);

    boolean removeVolunteer(Integer id);

    List<Integer> getVolChartData();

    Integer getSize();

}
