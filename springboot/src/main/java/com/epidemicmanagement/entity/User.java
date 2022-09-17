package com.epidemicmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName(value = "user")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String nickName;
    private String password;
    private String role;
    private String avatarUrl; // 头像
    private Integer isEnable; // 是否启用【0-未启用，1-启用】
}
