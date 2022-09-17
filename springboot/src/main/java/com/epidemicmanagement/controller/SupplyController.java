package com.epidemicmanagement.controller;

import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.entity.Supply;
import com.epidemicmanagement.entity.SupplyTakeInfo;
import com.epidemicmanagement.entity.User;
import com.epidemicmanagement.entity.Volunteer;
import com.epidemicmanagement.service.SupplyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.PrinterURI;
import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * @author XUE
 */
@Controller
@RequestMapping(value = "/api")
public class SupplyController {

    @Autowired
    private SupplyService supplyService;

    @ResponseBody
    @GetMapping(value = "/getSupplyList")
    public Result getSupplyList(@RequestParam("pageNum") Integer pageNum,
                                @RequestParam("pageSize") Integer pageSize) {
        PageInfo<Supply> supplyList = supplyService.getSupplyList(pageNum, pageSize);
        return Result.success(200, "查询成功").setAttribute("info", supplyList);
    }

    @ResponseBody
    @GetMapping(value = "/getTypeList")
    public Result getTypeList(@RequestParam("pageNum") Integer pageNum,
                              @RequestParam("pageSize") Integer pageSize,
                              @RequestParam("type") String type) {
        PageInfo<Supply> supplyList = supplyService.getTypeList(pageNum, pageSize, type);
        return Result.success(200, "查询成功").setAttribute("info", supplyList);
    }

    @ResponseBody
    @GetMapping(value = "/getSearchList")
    public Result getSearchList(@RequestParam("pageNum") Integer pageNum,
                              @RequestParam("pageSize") Integer pageSize,
                              @RequestParam("name") String name,
                                @RequestParam("type") String type) {
        PageInfo<Supply> supplyList = supplyService.getSearchList(pageNum, pageSize, name, type);
        return Result.success(200, "查询成功").setAttribute("info", supplyList);
    }

    @ResponseBody
    @GetMapping(value = "/getTakeInfoList")
    public Result getTakeInfoList(@RequestParam("pageNum") Integer pageNum,
                                  @RequestParam("pageSize") Integer pageSize,
                                  @RequestParam("id") Integer id) {
        PageInfo<SupplyTakeInfo> takeInfoList = supplyService.getTakeInfoList(pageNum, pageSize, id);
        return Result.success(200, "查询成功").setAttribute("infoList", takeInfoList);
    }

    @ResponseBody
    @GetMapping(value = "/getById")
    public Result getById(@RequestParam("id") Integer id) {
        Supply supply = supplyService.getById(id);
        return Result.success(200, "查询成功").setAttribute("supply", supply);
    }

    @ResponseBody
    @PostMapping(value = "/uploadSupplyImg")
    public Result uploadImg(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf('.'));
        fileName = UUID.randomUUID() + suffixName;
        String filePath = "C:\\Users\\hp\\IdeaProjects\\epidemic-management\\src\\main\\resources\\static\\supplyimg\\";
        try {
            file.transferTo(new File(filePath + fileName));
            return Result.success(200, "图片上传成功").setAttribute("imgName", fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "上传失败");
        }
    }

    @ResponseBody
    @PostMapping(value = "/supplyTake")
    public Result supplyTake(@RequestBody Map<String, Object> map) {
        Integer volId = (Integer) map.get("volId");
        Integer supplyId = (Integer) map.get("supplyId");
        Integer quantity = (Integer) map.get("quantity");
        String name = (String) map.get("name");
        String phone = (String) map.get("phone");
        String sex = (String) map.get("sex");
        SupplyTakeInfo info = new SupplyTakeInfo();
        info.setVolId(volId);
        info.setSupId(supplyId);
        info.setCnt(quantity);
        info.setName(name);
        info.setPhone(phone);
        info.setSex(sex);
        boolean flag = supplyService.insertSupplyVolRel(info);
        if (flag) {
            return Result.success(200, "插入成功");
        } else {
            return Result.fail(500, "插入失败");
        }
    }


    @ResponseBody
    @PostMapping(value = "/insertSupply")
    public Result insertSupply(@RequestBody Supply supply) {
        boolean flag = supplyService.insertSupply(supply);
        if (flag) {
            return Result.success(200, "插入成功");
        } else {
            return Result.fail(500, "插入失败");
        }
    }

    @ResponseBody
    @PostMapping(value = "/updateSupply")
    public Result updateSupply(@RequestBody Supply supply) {
        boolean flag = supplyService.updateSupply(supply);
        if (flag) {
            return Result.success(200, "更新成功");
        } else {
            return Result.fail(500, "更新失败");
        }
    }

}
