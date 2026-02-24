package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 积分规则
 *
 * @author dingyangmall
 * @date 2026-02-12
 */
@Data
@TableName("tb_integral_rule")
@EqualsAndHashCode(callSuper = true)
public class TbIntegralRule extends Model<TbIntegralRule> {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 注册赠送积分
     */
    private Integer registerIntegral;

    /**
     * 首充赠送积分
     */
    private Integer firstRechargeIntegral;

    /**
     * 签到赠送积分
     */
    private Integer signIntegral;

    /**
     * 推荐注册赠送积分
     */
    private Integer recommendIntegral;

    /**
     * 红包开关（0:关闭 1:开启）
     */
    private Integer redPacketSwitch;

    /**
     * 创建人
     */
    @TableField(exist = false)
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(exist = false)
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    private Integer delFlag;
}
