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
@TableName(value = "volunteer")
public class Volunteer {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("`name`")
    private String name;
    private Integer age;
    private String sex;
    private String phone;
    private String politicsStatus; // 政治面貌【共产党员、共青团员、群众、其他】
    private String vaccineStatus; // 疫苗接种情况【已接种完成、已接种一针/两针、未接种】
    private String detailedAddress;
    @TableField("`desc`")
    private String desc;
    private String imgUrl; // 健康码
    private String photoUrl; // 头像
    private Long insertTime;
    private Integer isEnable; // 是否启用：0-未启用；1-启用
    private Integer isDeny; // 申请是否被拒绝：0-未拒绝；1-被拒绝
    private Integer userId; // 登录用户ID

    @TableField(exist = false)
    private List<Task> taskList;
}
