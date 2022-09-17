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
@TableName(value = "vol_sup_rel")
public class SupplyTakeInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer supId;
    private Integer volId; // 志愿者信息
    private String name;
    private String phone;
    private String sex;
    private Integer cnt; // 数量
    private Long insertTime;

}
