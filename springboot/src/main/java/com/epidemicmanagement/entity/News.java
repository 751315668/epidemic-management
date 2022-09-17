package com.epidemicmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XUE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "news")
public class News {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    @TableField(value = "`desc`")
    private String desc;
    private String content;
    private String imgUrl;
    private Long createTime;

}
