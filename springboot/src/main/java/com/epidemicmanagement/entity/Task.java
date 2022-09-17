package com.epidemicmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author XUE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "task")
public class Task {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String publisher;
    private Integer publisherId;
    private Integer participantCnt; // 参与人数
    private String status; // 任务状态【0-审核中；1-进行中；2-已完成；3-取消】
    @TableField(value = "`desc`")
    private String desc;
    private Long startTime;
    private Long endTime;
    private Long insertTime;

    @TableField(exist = false)
    private List<Volunteer> volunteerList;

}
