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
@TableName(value = "supply")
public class Supply {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String type;
    private Integer quantity;
    @TableField(value = "`desc`")
    private String desc;
    private Integer leftNum;
    private String imgUrl;
    private Long insertTime;

    @TableField(exist = false)
    private List<SupplyTakeInfo> infoList; // 物资取用记录集合

}
