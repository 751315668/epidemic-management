package com.epidemicmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XUE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "message")
public class Message {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parent;
    private Integer userId; // 游客：0；管理员：1；志愿者：自身ID
    private String name;
    private String content;
    private String imgUrl;
    private Long insertTime;

    @TableField(exist = false)
    private List<Message> childrenList = new ArrayList<>();
}
