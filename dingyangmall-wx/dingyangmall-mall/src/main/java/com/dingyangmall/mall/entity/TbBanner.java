package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 首页轮播图
 *
 * @author dingyangmall
 */
@Data
@TableName("tb_banner")
@EqualsAndHashCode(callSuper = true)
public class TbBanner extends Model<TbBanner> {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 跳转链接(商品ID或外部链接)
     */
    private String linkUrl;

    /**
     * 跳转类型(0:无跳转; 1:商品详情; 2:外部链接)
     */
    private String linkType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态(0:下架; 1:上架)
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标志(0:正常; 1:删除)
     */
    @TableLogic
    private String delFlag;
}
