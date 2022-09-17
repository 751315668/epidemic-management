package com.epidemicmanagement.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.epidemicmanagement.aspect.PassInterceptor;
import com.epidemicmanagement.aspect.TokenHandler;
import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.entity.User;
import com.epidemicmanagement.entity.Volunteer;
import com.epidemicmanagement.service.SupplyService;
import com.epidemicmanagement.service.TaskService;
import com.epidemicmanagement.service.UserService;
import com.epidemicmanagement.service.VolunteerService;
import com.epidemicmanagement.utils.Token;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author XUE
 */
@Controller
@RequestMapping(value = "/api")
public class AppController {

    @Resource
    private UserService userService;
    @Resource
    private VolunteerService volunteerService;
    @Resource
    private TaskService taskService;
    @Resource
    private SupplyService supplyService;


    @PassInterceptor
    @ResponseBody
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, Object> map) {
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        Result result = new Result();
        User user = userService.login(username, password);
        if (user != null) {
            String token = Token.createToken(user.getId());
            result.setAttribute("token", token);
            result.setAttribute("nickname", user.getNickName());
            result.put("code", 200);
            result.put("msg", "登录成功");
        } else {
            result.put("code", 500);
            result.put("msg", "登录失败");
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/logout")
    public Result logout(@RequestHeader(value = "user_token") String token) {
        if (Token.verify(token)) {
            return Result.success(200, "注销成功");
        } else {
            return Result.fail(500, "注销失败");
        }
    }


    @ResponseBody
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@TokenHandler User user) {
        user = userService.findUserById(user.getId());
        Result result = new Result();
        String role = user.getRole();
        if ("guest".equals(role)) {
            result.setAttribute("roles", new Object[]{"guest"});
        } else if ("volunteer".equals(role)) {
            result.setAttribute("roles", new Object[]{"volunteer"});
        } else if ("admin".equals(role)) {
            result.setAttribute("roles", new Object[]{"admin"});
        }
        result.setAttribute("name", user.getNickName());
        result.setAttribute("avatar", user.getAvatarUrl());
        result.setAttribute("id", user.getId());
        // 获取登录用户对应的志愿者
        Volunteer volunteer = volunteerService.findByUserId(user.getId());
        if (volunteer != null) {
            result.setAttribute("volunteer", volunteer);
        }
        result.put("code", 200);
        result.put("msg", "获取角色信息成功");
        return result;
    }

    @ResponseBody
    @GetMapping("/findById")
    public Result findById(@RequestParam Integer id) {
        User user = userService.findUserById(id);
        if (user != null) {
            return Result.success(200, "获取成功").setAttribute("user", user);
        } else {
            return Result.fail(500, "获取失败");
        }
    }

    @ResponseBody
    @GetMapping("/getChartData")
    public Result getChartData() {
        List<Integer> volChartData = volunteerService.getVolChartData();
        List<Integer> taskChartData = taskService.getTaskChartData();
        List<Integer> supplyChartData = supplyService.getSupplyChartData();

        return Result.success(200, "获取成功")
                .setAttribute("volChartData", volChartData)
                .setAttribute("volSize", volunteerService.getSize())
                .setAttribute("taskChartData", taskChartData)
                .setAttribute("taskSize", taskService.getSize())
                .setAttribute("supplyChartData", supplyChartData)
                .setAttribute("supplySize", supplyService.getSize());
    }

    @ResponseBody
    @GetMapping("/checkName")
    public Result checkName(@RequestParam String name) {
        boolean flag = userService.checkName(name);
        if (flag) {
            return Result.success(200, "验证通过");
        } else {
            return Result.fail(200, "用户名不可用");
        }
    }

    @ResponseBody
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {
        boolean flag = userService.updateUser(user);
        if (flag) {
            return Result.success(200, "更新成功");
        } else {
            return Result.fail(500, "更新失败");
        }
    }

}
