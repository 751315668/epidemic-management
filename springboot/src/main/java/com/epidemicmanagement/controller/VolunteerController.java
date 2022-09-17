package com.epidemicmanagement.controller;

import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.entity.News;
import com.epidemicmanagement.entity.User;
import com.epidemicmanagement.entity.Volunteer;
import com.epidemicmanagement.service.UserService;
import com.epidemicmanagement.service.VolunteerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author XUE
 */
@Controller
@RequestMapping(value = "/api")
public class VolunteerController {

    @Resource
    private VolunteerService volunteerService;

    @Resource
    private UserService userService;

    @ResponseBody
    @GetMapping(value = "/getVolunteerList")
    public Result getVolunteerList(@RequestParam("pageNum") Integer pageNum,
                                   @RequestParam("pageSize") Integer pageSize) {
        PageInfo<Volunteer> volunteerPageInfo = volunteerService.getVolunteerList(pageNum, pageSize);
        return Result.success(200, "查询成功").setAttribute("info", volunteerPageInfo);
    }

    @ResponseBody
    @GetMapping(value = "/searchVolunteerList")
    public Result searchVolunteerList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("param") String param) {
        PageInfo<Volunteer> volunteerPageInfo = volunteerService.searchVolunteerList(pageNum, pageSize, param);
        return Result.success(200, "查询成功").setAttribute("info", volunteerPageInfo);
    }

    @ResponseBody
    @GetMapping(value = "/findVolById")
    public Result findVolById(@RequestParam("id") Integer id) {
        Volunteer vol = volunteerService.findById(id);
        if (vol != null) {
            return Result.success(200, "查询成功").setAttribute("volunteer", vol);
        } else {
            return Result.fail(500, "查询失败");
        }
    }

    @ResponseBody
    @GetMapping(value = "/getApplyList")
    public Result getApplyList(@RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        PageInfo<Volunteer> volunteerPageInfo = volunteerService.getApplyList(pageNum, pageSize);
        return Result.success(200, "查询成功").setAttribute("info", volunteerPageInfo);
    }

    @ResponseBody
    @PostMapping(value = "/insertVolunteer")
    public Result insertVolunteer(@RequestBody Map<String, Object> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Volunteer volunteer = mapper.convertValue(data.get("volunteer"), Volunteer.class);
        User user = mapper.convertValue(data.get("form"), User.class);
        user.setNickName("志愿者");
        user.setRole("volunteer");
        user.setAvatarUrl(volunteer.getPhotoUrl());
        user.setIsEnable(0);
        boolean flag = userService.insertUser(user);
        Integer userId = user.getId();
        volunteer.setUserId(userId);
        boolean flag2 = volunteerService.insertVolunteer(volunteer);
        if (flag && flag2) {
            return Result.success(200, "插入成功");
        } else {
            return Result.fail(500, "插入失败");
        }
    }

    // 同意志愿者申请
    @ResponseBody
    @PutMapping(value = "/acceptApply")
    public Result acceptApply(@RequestBody Map<String, Integer> map) {
        Integer id = map.get("id");
        boolean flag = volunteerService.acceptApply(id);
        if (flag) {
            return Result.success(200, "操作成功");
        } else {
            return Result.fail(500, "操作失败");
        }
    }

    @ResponseBody
    @PutMapping(value = "/denyApply")
    public Result denyApply(@RequestBody Map<String, Integer> map) {
        Integer id = map.get("id");
        boolean flag = volunteerService.denyApply(id);
        if (flag) {
            return Result.success(200, "操作成功");
        } else {
            return Result.fail(500, "操作失败");
        }
    }

    @ResponseBody
    @PutMapping(value = "/updateVolunteer")
    public Result updateVolunteer(@RequestBody Volunteer volunteer) {
        boolean flag = volunteerService.updateVolunteer(volunteer);
        if (flag) {
            return Result.success(200, "更新成功");
        } else {
            return Result.fail(500, "更新失败");
        }
    }

    @ResponseBody
    @GetMapping(value = "/removeVolunteer")
    public Result removeVolunteer(@RequestParam("id") Integer id) {
        boolean flag = volunteerService.removeVolunteer(id);
        if (flag) {
            return Result.success(200, "移除成功");
        } else {
            return Result.fail(500, "移除失败");
        }
    }

}
